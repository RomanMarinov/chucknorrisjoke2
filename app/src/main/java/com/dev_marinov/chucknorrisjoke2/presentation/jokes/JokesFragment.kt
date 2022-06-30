package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import android.content.res.Configuration
import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JokesFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private lateinit var jokesViewModel: JokesViewModel

    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initInterFace(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpCategoriesRecyclerView()
        setUpJokeTextView()
        setUpWidthTextViewCategory()
    }

    private fun initInterFace(inflater: LayoutInflater, container: ViewGroup?): View {
        container?.removeAllViewsInLayout()

        val orientation = requireActivity().resources.configuration.orientation
        val layoutResId = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            R.layout.fragment_list
        } else {
            R.layout.fragment_list
        }

        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    private fun initViewModel() {
        jokesViewModel = ViewModelProvider(this)[JokesViewModel::class.java]
    }

    private fun setUpCategoriesRecyclerView() {
        val adapter = CategoryAdapter(jokesViewModel)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerView.apply {
            layoutManager = this@JokesFragment.layoutManager
            this.adapter = adapter
        }

        jokesViewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.refreshCategories(categories)
            jokesViewModel.onCategoryClicked(categories[jokesViewModel.selectedPosition])
        }
    }

    private fun setUpJokeTextView() {
        jokesViewModel.joke.observe(viewLifecycleOwner) {
            binding.tvJoke.text = it
        }
    }

    private fun setUpWidthTextViewCategory() {
        jokesViewModel.widthTextViewCategory.observe(viewLifecycleOwner) {
            calculateWindowsMetrics(it)
        }
    }

    // метод получения размеров view чтобы переместить на центр экрана
    private fun calculateWindowsMetrics(widthTextViewCategory: Int) {
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
                layoutManager?.scrollToPositionWithOffset(
                    jokesViewModel.selectedPosition,
                    offset
                )
            } catch (e: java.lang.Exception) {
                Log.e("333", "-try catch FragmentList 1-$e")
            }
        }
    }
}