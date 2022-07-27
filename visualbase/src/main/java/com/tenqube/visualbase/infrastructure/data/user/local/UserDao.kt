package com.tenqube.visualbase.infrastructure.data.user.local

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): UserModel?

    @Insert
    fun save(item: UserModel)

    @Update
    fun update(item: UserModel)

    @Delete
    fun delete(item: UserModel)
}
