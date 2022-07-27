package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.category.Category

@Entity(tableName = "category")
data class CategoryModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "large")
    val large: String,
    @ColumnInfo(name = "medium")
    val medium: String,
    @ColumnInfo(name = "small")
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
