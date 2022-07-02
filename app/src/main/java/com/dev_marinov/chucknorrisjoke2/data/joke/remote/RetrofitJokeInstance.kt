package com.dev_marinov.chucknorrisjoke2.data.joke.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitJokeInstance {

    companion object {
        fun getRetroJoke(): Retrofit {
            val baseUrl = "https://api.chucknorris.io/jokes/"
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}