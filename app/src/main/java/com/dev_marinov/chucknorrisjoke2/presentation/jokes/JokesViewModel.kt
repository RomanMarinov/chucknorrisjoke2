package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import android.util.Log
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


class JokesViewModel : ViewModel() {

    var selectedPosition = 6
    var widthTextViewCategory = 213

    private val _categories: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val categories: LiveData<ArrayList<String>> = _categories

    private val _joke: MutableLiveData<String> = MutableLiveData()
    val joke: LiveData<String> = _joke

    init {
        getCategories()

        categories.value?.let {
            if (it.isNotEmpty())
                getJoke(it[selectedPosition])
        }
    }

    fun onCategoryClicked(category: String) = getJoke(category)

    private fun getJoke(category: String) {
        Log.d("333", "=makeApiCall=")

        // GlobalScope - карутин верхнего уровня и живет столько сколько живет все приложение
        // при этом если фрагмент или активность будут уничтожены где мы используем GlobalScope
        // то GlobalScope не будет уничтожен и это может привести к утечке памяти
        // Поэтому карутин должен привязан к жизненому циклу (к опреденному компоненту).
        // lifecycleScope для фрагментов, viewModelScope - для viewModel

        viewModelScope.launch(Dispatchers.IO) {

            val retroInstance = RetroJokeInstance.getRetroJoke().create(RetroJokeApi::class.java)
            val response = retroInstance.getJoke(category)
            if (response.isSuccessful) {
                _joke.postValue(response.body()!!.value)
            }
        }

    }

    private fun getCategories() {
        Log.d("333", "=makeApiCall=")

        // GlobalScope - карутин верхнего уровня и живет столько сколько живет все приложение
        // при этом если фрагмент или активность будут уничтожены где мы используем GlobalScope
        // то GlobalScope не будет уничтожен и это может привести к утечке памяти
        // Поэтому карутин должен привязан к жизненому циклу (к опреденному компоненту).
        // lifecycleScope для фрагментов, viewModelScope - для viewModel

        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance =
                RetroCategoriesInstance.getRetroCategory().create(RetroCategoriesApi::class.java)
            val response = retroInstance.getCategories()
            if (response.isSuccessful) {
                val list: ArrayList<String> = ArrayList()
                response.body()?.let {
                    it.forEach { category ->
                        list.add(category)
                    }
                    // setValue уместен в основном потоке приложения,
                    // а postValue — если данные приходят из фонового потока.
                    _categories.postValue(list)
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
}
