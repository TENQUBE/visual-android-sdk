package com.tenqube.visualbase.infrastructure.data.category

import com.tenqube.visualbase.domain.category.Category
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override suspend fun findAll(): Result<List<Category>> {
        return dao.getAll().map {
            it.asDomain()
        }
    }
}