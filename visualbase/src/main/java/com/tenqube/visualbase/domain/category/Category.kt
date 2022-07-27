package com.tenqube.visualbase.domain.category

import com.tenqube.visualbase.domain.Entity

data class Category(
    override val id: String,
    val code: String,
    val large: String,
    val medium: String,
    val small: String
) : Entity
