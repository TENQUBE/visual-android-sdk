package com.tenqube.visualbase.domain.transaction

interface TransactionRepository {

    suspend fun findById(id: String): Result<Transaction>

    suspend fun findAll(): Result<List<Transaction>>

    suspend fun save(item: Transaction): Result<Transaction>

    suspend fun delete(item: Transaction): Result<Transaction>
}