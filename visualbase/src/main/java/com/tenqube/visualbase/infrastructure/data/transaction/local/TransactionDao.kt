package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.*
import com.tenqube.visualbase.domain.transaction.dto.CountByNoti

@Dao
interface TransactionDao {

    @Query("SELECT originTel as name, COUNT(*) as count FROM transactionModel WHERE smsType = 0 GROUP BY originTel")
    suspend fun getGroupByNoti(): List<CountByNoti>

    @Query("SELECT * FROM transactionModel WHERE spentDate BETWEEN :from ANd :to")
    suspend fun getByFilter(from: String, to: String): List<TransactionModel>

    @Query("SELECT * FROM transactionModel ORDER BY spentDate DESC")
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
