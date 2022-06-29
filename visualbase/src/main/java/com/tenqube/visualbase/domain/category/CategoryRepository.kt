package com.tenqube.visualbase.domain.category

interface CategoryRepository {
    suspend fun findAll(): Result<List<Category>>
}