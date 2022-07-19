package com.tenqube.visualbase.infrastructure.data.user.local

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM userModel")
    fun getAll(): List<UserModel>

    @Insert
    fun insertAll(vararg items: UserModel)

    @Update
    fun update(item: UserModel)

    @Delete
    fun delete(item: UserModel)
}
