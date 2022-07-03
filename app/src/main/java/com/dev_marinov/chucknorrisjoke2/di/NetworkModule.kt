package com.dev_marinov.chucknorrisjoke2.di

import com.dev_marinov.chucknorrisjoke2.data.category.remote.CategoryService
import com.dev_marinov.chucknorrisjoke2.data.category.remote.RetrofitCategoriesInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = "https://api.chucknorris.io/jokes/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}