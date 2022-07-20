package com.tenqube.visualbase.infrastructure.data.user.local

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM userModel LIMIT 1")
    suspend fun getUser(): UserModel

    @Insert
    suspend fun insertAll(vararg items: UserModel)

    @Update
    suspend fun update(item: UserModel)

    @Delete
    suspend fun delete(item: UserModel)
}
