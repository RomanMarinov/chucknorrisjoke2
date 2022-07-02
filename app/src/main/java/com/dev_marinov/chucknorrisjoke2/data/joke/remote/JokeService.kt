package com.dev_marinov.chucknorrisjoke2.data.joke.remote

import com.dev_marinov.chucknorrisjoke2.data.Joke
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// интерфейс апи для получения шутки
// корутин suspend для работы с асинхронноым запросом
// запрос с параметром
interface JokeService {

    @GET("random")
    suspend fun getJoke(
        @Query("category")category: String
    ): Response<Joke>

}