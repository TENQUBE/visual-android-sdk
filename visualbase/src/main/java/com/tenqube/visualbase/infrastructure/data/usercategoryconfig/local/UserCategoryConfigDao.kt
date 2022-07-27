package com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local

import androidx.room.*

@Dao
interface UserCategoryConfigDao {
    @Query("SELECT * FROM userCategoryConfig")
    fun getAll(): List<UserCategoryConfigModel>

    @Query("SELECT * FROM userCategoryConfig WHERE id = :id")
    fun getById(id: String): UserCategoryConfigModel?

    @Insert
    fun insertAll(items: UserCategoryConfigModel)

    @Update
    fun update(item: UserCategoryConfigModel)

    @Delete
    fun delete(item: UserCategoryConfigModel)
}
