package com.dev_marinov.chucknorrisjoke2.presentation.model

import com.dev_marinov.chucknorrisjoke2.domain.Category

data class SelectableCategory(
    val name: String,
    val isSelected: Boolean
) {

    fun mapToDomain(): Category = Category(name = this.name)
}
