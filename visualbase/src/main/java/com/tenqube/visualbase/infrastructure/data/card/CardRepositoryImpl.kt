package com.tenqube.visualbase.infrastructure.data.card

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.card.local.CardModel

class CardRepositoryImpl(private val dao: CardDao) : CardRepository {
    override suspend fun findAll(): Result<List<Card>> {
        return try {
            Result.success(
                dao.getAll().map { it.asDomain() }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findById(id: String): Result<Card> {
        return try {
            Result.success(
                dao.getById(id).asDomain()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun save(items: List<Card>): Result<Unit> {
        return try {
            Result.success(
                items.forEach {
                    dao.insertAll(CardModel.fromDomain(it))
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(items: List<Card>): Result<Unit> {
        return try {
            Result.success(
                items.forEach {
                    dao.update(CardModel.fromDomain(it))
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(items: List<Card>): Result<Unit> {
        return try {
            Result.success(
                items.forEach {
                    dao.delete(CardModel.fromDomain(it))
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}