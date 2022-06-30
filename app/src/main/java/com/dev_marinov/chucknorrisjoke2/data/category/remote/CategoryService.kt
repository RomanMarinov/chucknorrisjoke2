package com.dev_marinov.chucknorrisjoke2.data.category.remote

import retrofit2.Response
import retrofit2.http.GET

interface CategoryService {

    @GET("categories")
    suspend fun getCategories(): Response<ArrayList<String>>
}