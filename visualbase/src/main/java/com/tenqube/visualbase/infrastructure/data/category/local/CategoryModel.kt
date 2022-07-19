package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryModel(
    @PrimaryKey val id: String,
    val code: String,
    val large: String,
    val medium: String,
    val small: String
)