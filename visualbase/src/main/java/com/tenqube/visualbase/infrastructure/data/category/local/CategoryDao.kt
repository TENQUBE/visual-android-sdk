package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categoryModel")
    suspend fun getAll(): List<CategoryModel>

    @Query("SELECT * FROM categoryModel WHERE id = :id")
    suspend fun getById(id: String): CategoryModel?

    @Insert
    suspend fun insertAll(items: CategoryModel)

    @Update
    suspend fun update(item: CategoryModel)

    @Delete
    suspend fun delete(item: CategoryModel)
}
