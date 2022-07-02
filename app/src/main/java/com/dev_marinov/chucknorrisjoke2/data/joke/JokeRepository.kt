package com.dev_marinov.chucknorrisjoke2.data.joke

import com.dev_marinov.chucknorrisjoke2.data.joke.remote.JokeService
import com.dev_marinov.chucknorrisjoke2.data.joke.remote.RetrofitJokeInstance
import com.dev_marinov.chucknorrisjoke2.presentation.jokes.Category

object JokeRepository {
    private val jokeDataSource: JokeService =
        RetrofitJokeInstance.getRetroJoke().create(JokeService::class.java)

    suspend fun getJoke(category: Category): String {
        val response = jokeDataSource.getJoke(category = category.name)
        val joke = response.body()?.let { it.value } ?: ""
        return joke
    }
}