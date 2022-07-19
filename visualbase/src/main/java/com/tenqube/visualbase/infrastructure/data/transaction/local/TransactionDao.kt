package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactionModel")
    fun getAll(): List<TransactionModel>

    @Insert
    fun insertAll(vararg items: TransactionModel)

    @Update
    fun update(item: TransactionModel)

    @Delete
    fun delete(item: TransactionModel)
}