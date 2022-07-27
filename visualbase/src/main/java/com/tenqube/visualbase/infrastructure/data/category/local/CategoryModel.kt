package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.category.Category

@Entity
data class CategoryModel(
    @PrimaryKey val id: String,
    val code: String,
    val large: String,
    val medium: String,
    val small: String
) {
    fun asDomain(): Category {
        return Category(
            id,
            code,
            large,
            medium,
            small
        )
    }

    companion object {
        fun fromDomain(category: Category): CategoryModel {
            return CategoryModel(
                category.id,
                category.code,
                category.large,
                category.medium,
                category.small
            )
        }
    }
}
