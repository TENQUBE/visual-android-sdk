package com.tenqube.visualbase.domain.usercategoryconfig

data class UserCategoryConfig(
    val id: String,
    val userId: String,
    val code: Int,
    val type: String,
    val isExcept: Boolean,
    val isMain: Boolean
)