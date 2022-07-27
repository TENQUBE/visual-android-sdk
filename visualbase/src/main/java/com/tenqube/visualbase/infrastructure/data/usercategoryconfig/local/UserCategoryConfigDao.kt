package com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local

import androidx.room.*

@Dao
interface UserCategoryConfigDao {
    @Query("SELECT * FROM userCategoryConfigModel")
    suspend fun getAll(): List<UserCategoryConfigModel>

    @Query("SELECT * FROM userCategoryConfigModel WHERE id = :id")
    suspend fun getById(id: String): UserCategoryConfigModel?

    @Insert
    suspend fun insertAll(vararg items: UserCategoryConfigModel)

    @Update
    suspend fun update(item: UserCategoryConfigModel)

    @Delete
    suspend fun delete(item: UserCategoryConfigModel)
}
