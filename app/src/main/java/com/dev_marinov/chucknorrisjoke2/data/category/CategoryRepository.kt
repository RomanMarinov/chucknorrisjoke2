package com.dev_marinov.chucknorrisjoke2.data.category

import com.dev_marinov.chucknorrisjoke2.data.category.remote.CategoryService
import com.dev_marinov.chucknorrisjoke2.data.category.remote.RetrofitCategoriesInstance

object CategoryRepository {
    private val remoteDataSource: CategoryService =
        RetrofitCategoriesInstance.getRetrofit().create(CategoryService::class.java)

    suspend fun getCategories(): List<String> {
        val response = remoteDataSource.getCategories()
        val categories = response.body()?.let { it } ?: listOf<String>()
        return categories
    }
}