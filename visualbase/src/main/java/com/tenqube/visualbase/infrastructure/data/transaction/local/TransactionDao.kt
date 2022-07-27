package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.*
import com.tenqube.visualbase.domain.transaction.dto.CountByNoti

@Dao
interface TransactionDao {

    @Query("SELECT originTel as name, COUNT(*) as count FROM `transaction` WHERE smsType = 0 GROUP BY originTel")
    fun getGroupByNoti(): List<CountByNoti>

    @Query("SELECT * FROM `transaction` WHERE spentDate BETWEEN :from ANd :to")
    fun getByFilter(from: String, to: String): List<TransactionModel>

    @Query("SELECT * FROM `transaction` ORDER BY spentDate DESC")
    fun getAll(): List<TransactionModel>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getById(id: String): TransactionModel?

    @Insert
    fun insertAll(item: TransactionModel)

    @Update
    fun update(item: TransactionModel)

    @Delete
    fun delete(item: TransactionModel)
}
