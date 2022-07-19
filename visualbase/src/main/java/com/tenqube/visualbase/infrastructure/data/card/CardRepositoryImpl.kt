package com.tenqube.visualbase.infrastructure.data.card

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao

class CardRepositoryImpl(private val dao: CardDao) : CardRepository {
    override suspend fun findAll(): Result<List<Card>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): Result<Card> {
        TODO("Not yet implemented")
    }

    override suspend fun save(items: List<Card>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun update(items: List<Card>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(items: List<Card>): Result<Unit> {
        TODO("Not yet implemented")
    }
}