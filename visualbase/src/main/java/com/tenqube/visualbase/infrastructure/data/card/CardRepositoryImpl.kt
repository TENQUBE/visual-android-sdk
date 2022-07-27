package com.tenqube.visualbase.infrastructure.data.card

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.card.local.CardModel

class CardRepositoryImpl(private val dao: CardDao) : CardRepository {
    override suspend fun findAll(): List<Card> {
        return dao.getAll().map { it.asDomain() }
    }

    override suspend fun findById(id: String): Card? {
        return dao.getById(id)?.asDomain()
    }

    override suspend fun save(items: List<Card>) {
        items.forEach {
            dao.insertAll(CardModel.fromDomain(it))
        }
    }

    override suspend fun update(items: List<Card>) {
        items.forEach {
            dao.update(CardModel.fromDomain(it))
        }
    }

    override suspend fun delete(items: List<Card>) {
        items.forEach {
            dao.delete(CardModel.fromDomain(it))
        }
    }
}
