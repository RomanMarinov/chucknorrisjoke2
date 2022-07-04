package com.dev_marinov.chucknorrisjoke2.data.joke

import com.dev_marinov.chucknorrisjoke2.data.joke.remote.JokeService
import com.dev_marinov.chucknorrisjoke2.domain.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(private val remoteDataSource: JokeService) {

    suspend fun getJoke(category: Category): String {
        val response = remoteDataSource.getJoke(category = category.name)
        val joke = response.body()?.let { it.value } ?: ""
        return joke
    }
}
