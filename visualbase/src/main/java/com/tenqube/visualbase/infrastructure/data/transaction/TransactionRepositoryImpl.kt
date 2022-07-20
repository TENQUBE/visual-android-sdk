package com.tenqube.visualbase.infrastructure.data.transaction

import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter

class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {
    override suspend fun findById(id: String): Result<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Result<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override suspend fun findByFilter(filter: TransactionFilter): Result<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(item: Transaction): Result<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAll(items: List<Transaction>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(item: Transaction): Result<Transaction> {
        TODO("Not yet implemented")
    }
}