package com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserCategoryConfigModel(
    @PrimaryKey val id: String,
    val userId: String,
    val code: Int,
    val type: String,
    val isExcept: Boolean,
    val isMain: Boolean
)