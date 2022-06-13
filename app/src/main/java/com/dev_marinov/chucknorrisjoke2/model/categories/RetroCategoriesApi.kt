package com.dev_marinov.chucknorrisjoke2.model.categories

import retrofit2.Response
import retrofit2.http.GET
import kotlin.coroutines.suspendCoroutine

// интерфейс апи для получения массива категорий для шуток
// корутин suspend для работы с асинхронноым запросом
interface RetroCategoriesApi {

    // Interface - интерфейс для управления адресом, используя команды GET, POST и т.д.
    @GET("categories")
    suspend fun getCategories(): Response<ArrayList<String>>

}