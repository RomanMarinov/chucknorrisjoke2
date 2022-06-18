package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.window.layout.WindowMetricsCalculator
import com.dev_marinov.chucknorrisjoke2.R
import com.dev_marinov.chucknorrisjoke2.databinding.FragmentListBinding
import com.dev_marinov.chucknorrisjoke2.presentation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JokesFragment : Fragment() {

    private lateinit var binding: FragmentListBinding // биндинг для фрагмента

    private lateinit var jokesViewModel: JokesViewModel // для сохранения массива категорий

    lateinit var adapterListCategory: AdapterListCategory // адапетр для категорий шуток

    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initInterFace(inflater, container)
    }

    private fun initInterFace(inflater: LayoutInflater, container: ViewGroup?): View {
        // если уже есть надутый макет, удалить его.
        container?.let { it.removeAllViewsInLayout() }

        // получить экран ориентации
        val orientation = requireActivity().resources.configuration.orientation
        // раздуть соответствующий макет в зависимости от ориентации экрана
        binding = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        } else {
            DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        }
        // метод работы с views, adapter, recycler
        myRecyclerLayoutManagerAdapter()

        return binding.root
    }

    private fun myRecyclerLayoutManagerAdapter() {

        jokesViewModel = ViewModelProvider(this)[JokesViewModel::class.java]

        binding.tvTitle.text = resources.getString(R.string.choose)

        adapterListCategory = AdapterListCategory(jokesViewModel)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerView.apply {
            layoutManager = this@JokesFragment.layoutManager
            adapter = adapterListCategory
        }

        // слушатель кликов из адаптера чтобы запустить сетевой запрос и получить шутку joke
        adapterListCategory.setOnItemClickListener(object :
            AdapterListCategory.onItemClickListener {
            override fun onItemClick(
                position: Int,
                clickCategory: String,
                widthTextViewCategory: Int
            ) {

                jokesViewModel.onCategoryClicked(clickCategory)// api запрос шутки

                calculateWindowsMetrics(widthTextViewCategory) // смещение view на центр экрана
            }
        })

        jokesViewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapterListCategory.refreshUsers(categories)
            adapterListCategory!!.notifyDataSetChanged()

            jokesViewModel.onCategoryClicked(categories[jokesViewModel.selectedPosition])

            // метод для смещения view на центр экрана
            calculateWindowsMetrics(jokesViewModel.widthTextViewCategory)
        }

        // наблюдатель для получения шутки ответа от сети
        jokesViewModel.joke.observe(viewLifecycleOwner) { t ->
            Log.e("333", "=viewModelListCategory t=" + t)
            binding.tvJoke.text = t
        }

    }

    // метод получения размеров view чтобы переместить на центр экрана
    fun calculateWindowsMetrics(widthTextViewCategory: Int) {
        // получить офсет ширина экрана (можно тут получить) / 2 мнус длина текстВью / 2
        val windowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
        val width = currentBounds.width()

        // поскольку получить ширину TextViewCategory нельзя до ее появления на экране в холдере
        // я узнал ее ширину заранее (213 установил ее в ViewModelWidthTextViewCategory)
        // (эта позиция по умолчанию, установлена во ViewModelSelectPosition)
        val offset = (width / 2) - (widthTextViewCategory / 2)
        Log.d("333", "-widthTextViewCategory=" + widthTextViewCategory)

        // установка textView по центру  и позицию
        // использую viewModelSelectPos и viewModelWidthTextView

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                layoutManager?.let {
                    it.scrollToPositionWithOffset(
                        jokesViewModel.selectedPosition,
                        offset
                    )
                }
            } catch (e: java.lang.Exception) {
                Log.e("333", "-try catch FragmentList 1-$e")
            }
        }
    }
}