package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_marinov.chucknorrisjoke2.model.categories.RetroCategoriesApi
import com.dev_marinov.chucknorrisjoke2.model.categories.RetroCategoriesInstance
import com.dev_marinov.chucknorrisjoke2.model.joke.RetroJokeApi
import com.dev_marinov.chucknorrisjoke2.model.joke.RetroJokeInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JokesViewModel : ViewModel(), CategoryAdapter.OnItemClickListener {

    var selectedPosition = 6
    private val DEFAULT_WIDTH = 213

    private val _widthTextViewCategory = MutableLiveData<Int>()
    val widthTextViewCategory = _widthTextViewCategory

    private val _categories: MutableLiveData<ArrayList<Category>> = MutableLiveData()
    val categories: LiveData<ArrayList<Category>> = _categories

    private val _joke: MutableLiveData<String> = MutableLiveData()
    val joke: LiveData<String> = _joke

    init {
        getCategories()

        categories.value?.let {
            if (it.isNotEmpty())
                getJoke(it[selectedPosition])
        }
    }

    override fun onItemClick(position: Int, clickCategory: Category, widthTextViewCategory: Int) {
        selectedPosition = position
        updateCategories()
        getJoke(clickCategory)
        _widthTextViewCategory.value = widthTextViewCategory
    }

    fun onCategoryClicked(category: Category) = getJoke(category)

    private fun getJoke(category: Category) {
        // GlobalScope - карутин верхнего уровня и живет столько сколько живет все приложение
        // при этом если фрагмент или активность будут уничтожены где мы используем GlobalScope
        // то GlobalScope не будет уничтожен и это может привести к утечке памяти
        // Поэтому карутин должен привязан к жизненому циклу (к опреденному компоненту).
        // lifecycleScope для фрагментов, viewModelScope - для viewModel

        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance = RetroJokeInstance.getRetroJoke().create(RetroJokeApi::class.java)
            val response = retroInstance.getJoke(category.name)
            if (response.isSuccessful) {
                response.body()?.let { _joke.postValue(it.value) }
            }
        }
    }

    private fun getCategories() {
        // GlobalScope - корутина верхнего уровня и живет столько сколько живет все приложение
        // при этом если фрагмент или активность будут уничтожены где мы используем GlobalScope
        // то GlobalScope не будет уничтожен и это может привести к утечке памяти
        // Поэтому карутин должен привязан к жизненому циклу (к опреденному компоненту).
        // lifecycleScope для фрагментов, viewModelScope - для viewModel

        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance =
                RetroCategoriesInstance.getRetroCategory().create(RetroCategoriesApi::class.java)
            val response = retroInstance.getCategories()
            if (response.isSuccessful) {
                val list: ArrayList<Category> = ArrayList()
                response.body()?.let {
                    it.forEachIndexed { index, name ->
                        val category = Category(name = name, isSelected = index == selectedPosition)
                        list.add(category)
                    }
                    // setValue уместен в основном потоке приложения,
                    // а postValue — если данные приходят из фонового потока.
                    _categories.postValue(list)
                    _widthTextViewCategory.postValue(DEFAULT_WIDTH)
                }
            } else {
                when (response.code()) {
                    404 -> {
                        // страница не найдена. можно использовать ResponseBody, см. ниже
                    }
                    500 -> {
                        // ошибка на сервере. можно использовать ResponseBody, см. ниже
                    }
                }
            }
        }
    }

    private fun updateCategories() {
        val newCategories = arrayListOf<Category>()
        _categories.value?.let {
            it.forEachIndexed { index, category ->
                val newCategory = category.copy(isSelected = index == selectedPosition)
                newCategories.add(newCategory)
            }
        }
        _categories.value = newCategories
    }
}
