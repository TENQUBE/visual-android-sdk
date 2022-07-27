package com.tenqube.visualbase.domain.category

interface CategoryRepository {
    suspend fun findAll(): List<Category>
    suspend fun findById(id: String): Category?
    suspend fun save(items: List<Category>)
}
