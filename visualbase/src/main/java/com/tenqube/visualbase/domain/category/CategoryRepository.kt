package com.tenqube.visualbase.domain.category

interface CategoryRepository {
    suspend fun findAll(): Result<List<Category>>
    suspend fun save(items: List<Category>): Result<Unit>
}
