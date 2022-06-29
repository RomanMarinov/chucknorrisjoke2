package com.dev_marinov.chucknorrisjoke2.data.category.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ретрофит базовый урл
object RetrofitCategoriesInstance {

    //        Retrofit - класс для обработки результатов.
    fun getRetrofit(): Retrofit {
        val baseUrl = "https://api.chucknorris.io/jokes/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}