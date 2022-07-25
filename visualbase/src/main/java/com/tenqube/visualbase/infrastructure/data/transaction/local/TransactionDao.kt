package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactionModel WHERE spentDate BETWEEN :from ANd :to")
    suspend fun getByFilter(from: String, to: String): List<TransactionModel>

    @Query("SELECT * FROM transactionModel")
    suspend fun getAll(): List<TransactionModel>

    @Query("SELECT * FROM transactionModel WHERE id = :id")
    suspend fun getById(id: String): TransactionModel?

    @Insert
    suspend fun insertAll(vararg items: TransactionModel)

    @Update
    suspend fun update(item: TransactionModel)

    @Delete
    suspend fun delete(item: TransactionModel)
}