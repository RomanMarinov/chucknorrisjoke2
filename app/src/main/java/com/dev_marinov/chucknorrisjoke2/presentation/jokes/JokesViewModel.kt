package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_marinov.chucknorrisjoke2.data.category.CategoryRepository
import com.dev_marinov.chucknorrisjoke2.data.category.remote.CategoryService
import com.dev_marinov.chucknorrisjoke2.data.category.remote.RetrofitCategoriesInstance
import com.dev_marinov.chucknorrisjoke2.data.joke.JokeRepository
import com.dev_marinov.chucknorrisjoke2.domain.Category
import com.dev_marinov.chucknorrisjoke2.presentation.model.SelectableCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel(), CategoryAdapter.OnItemClickListener {

    var selectedPosition = 6
    private val DEFAULT_WIDTH = 213

    private val _widthTextViewCategory = MutableLiveData<Int>()
    val widthTextViewCategory = _widthTextViewCategory

    private val _categories: MutableLiveData<ArrayList<SelectableCategory>> = MutableLiveData()
    val categories: LiveData<ArrayList<SelectableCategory>> = _categories

    private val _joke: MutableLiveData<String> = MutableLiveData()
    val joke: LiveData<String> = _joke

    init {
        getCategories()

        categories.value?.let {
            if (it.isNotEmpty()) {
                val selectableCategory: SelectableCategory = it[selectedPosition]
                getJoke(selectableCategory.mapToDomain())
            }
        }
    }

    override fun onItemClick(
        position: Int,
        clickCategory: SelectableCategory,
        widthTextViewCategory: Int
    ) {
        selectedPosition = position
        updateCategories()
        getJoke(clickCategory.mapToDomain())
        _widthTextViewCategory.value = widthTextViewCategory
    }

    fun onCategoryClicked(category: SelectableCategory) = getJoke(category.mapToDomain())

    private fun getJoke(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            JokeRepository.getJoke(category).let {
                _joke.postValue(it)
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<SelectableCategory> = ArrayList()
            categoryRepository.getCategories().forEachIndexed { index, name ->
                val category =
                    SelectableCategory(name = name, isSelected = index == selectedPosition)
                list.add(category)
            }
            _categories.postValue(list)
            _widthTextViewCategory.postValue(DEFAULT_WIDTH)
        }
    }

    private fun updateCategories() {
        val newCategories = arrayListOf<SelectableCategory>()
        _categories.value?.let {
            it.forEachIndexed { index, category ->
                val newCategory = category.copy(isSelected = index == selectedPosition)
                newCategories.add(newCategory)
            }
        }
        _categories.value = newCategories
    }
}
