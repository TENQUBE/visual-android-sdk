package com.tenqube.visualbase.infrastructure.data.transaction

import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.domain.transaction.dto.CountByNoti
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionModel
import com.tenqube.visualbase.infrastructure.data.transaction.remote.TransactionRemoteDataSource
import com.tenqube.visualbase.infrastructure.data.transaction.remote.TransactionRequest
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter

class TransactionRepositoryImpl(
    private val dao: TransactionDao,
    private val remote: TransactionRemoteDataSource,
) : TransactionRepository {

    override suspend fun findCountByNoti(): List<CountByNoti> {
        return dao.getGroupByNoti()
    }

    override suspend fun findById(id: String): Transaction? {
        return dao.getById(id)?.asDomain()
    }

    override suspend fun findAll(): List<Transaction> {
        return dao.getAll().map { it.asDomain() }
    }

    override suspend fun findByFilter(filter: TransactionFilter): List<Transaction> {
        val query = filter.toQuery()
        return dao.getByFilter(from = query[0], to = query[1]).map { it.asDomain() }
    }

    override suspend fun save(item: Transaction) {
        dao.insertAll(TransactionModel.fromDomain(item))
    }

    override suspend fun update(item: Transaction) {
        dao.update(TransactionModel.fromDomain(item))
    }

    override suspend fun saveAll(items: List<JoinedTransaction>) {
        items.forEach {
            dao.insertAll(TransactionModel.fromDomain(it.transaction))
        }
        remote.saveTransaction(TransactionRequest.from(items))
    }

    override suspend fun delete(item: Transaction) {
        dao.delete(TransactionModel.fromDomain(item))
    }
}
