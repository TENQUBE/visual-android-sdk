package com.tenqube.visualbase.infrastructure.data.transaction

import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.infrastructure.data.card.local.CardModel
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionModel
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter

class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {
    override suspend fun findById(id: String): Result<Transaction> {
        return try {
            Result.success(
                dao.getById(id).asDomain()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findAll(): Result<List<Transaction>> {
        return try {
            Result.success(
                dao.getAll().map { it.asDomain() }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findByFilter(filter: TransactionFilter): Result<List<Transaction>> {
        return try {
            Result.success(
                dao.getAll().map { it.asDomain() }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun save(item: Transaction): Result<Unit> {
        return try {
            Result.success(
                dao.insertAll(TransactionModel.fromDomain(item))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(item: Transaction): Result<Unit> {
        return try {
            Result.success(
                dao.update(TransactionModel.fromDomain(item))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveAll(items: List<Transaction>): Result<Unit> {
        return try {
            Result.success(
                items.forEach {
                    dao.insertAll(TransactionModel.fromDomain(it))
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(item: Transaction): Result<Unit> {
        return try {
            Result.success(
                dao.delete(TransactionModel.fromDomain(item))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}