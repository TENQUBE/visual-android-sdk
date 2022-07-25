package com.tenqube.visualbase.domain.transaction

import com.tenqube.visualbase.service.transaction.dto.TransactionFilter

interface TransactionRepository {

    suspend fun findById(id: String): Transaction?

    suspend fun findAll(): List<Transaction>

    suspend fun findByFilter(filter: TransactionFilter): List<Transaction>

    suspend fun save(item: Transaction)

    suspend fun update(item: Transaction)

    suspend fun saveAll(items: List<Transaction>)

    suspend fun delete(item: Transaction)
}
