package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): List<CategoryModel>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getById(id: String): CategoryModel?

    @Insert
    fun insertAll(items: CategoryModel)

    @Update
    fun update(item: CategoryModel)

    @Delete
    fun delete(item: CategoryModel)
}
