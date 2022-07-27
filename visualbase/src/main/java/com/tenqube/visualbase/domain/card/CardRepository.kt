package com.tenqube.visualbase.domain.card

interface CardRepository {

    suspend fun findAll(): List<Card>

    suspend fun findById(id: String): Card?

    suspend fun save(items: List<Card>)

    suspend fun update(items: List<Card>)

    suspend fun delete(items: List<Card>)
}
