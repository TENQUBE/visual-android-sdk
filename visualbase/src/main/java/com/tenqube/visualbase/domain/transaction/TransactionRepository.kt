package com.tenqube.visualbase.domain.transaction

import com.tenqube.visualbase.service.transaction.dto.TransactionFilter

interface TransactionRepository {

    suspend fun findById(id: String): Result<Transaction>

    suspend fun findAll(): Result<List<Transaction>>

    suspend fun findByFilter(filter: TransactionFilter): Result<List<Transaction>>

    suspend fun save(item: Transaction): Result<Unit>

    suspend fun update(item: Transaction): Result<Unit>

    suspend fun saveAll(items: List<Transaction>): Result<Unit>

    suspend fun delete(item: Transaction): Result<Unit>
}
