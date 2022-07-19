package com.tenqube.visualbase.infrastructure.data.category.local

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categoryModel")
    fun getAll(): List<CategoryModel>

    @Insert
    fun insertAll(vararg items: CategoryModel)

    @Update
    fun update(item: CategoryModel)

    @Delete
    fun delete(item: CategoryModel)
}