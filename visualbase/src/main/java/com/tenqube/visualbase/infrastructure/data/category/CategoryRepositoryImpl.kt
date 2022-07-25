package com.tenqube.visualbase.infrastructure.data.category

import com.tenqube.visualbase.domain.category.Category
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryModel

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override suspend fun findAll(): List<Category> {
        return dao.getAll().map { it.asDomain() }
    }

    override suspend fun findById(id: String): Category? {
        return dao.getById(id)?.asDomain()
    }

    override suspend fun save(items: List<Category>) {
        items.forEach {
            dao.insertAll(CategoryModel.fromDomain(it))
        }
    }
}