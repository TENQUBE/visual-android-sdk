package com.tenqube.visualbase.domain.card

interface CardRepository {

    suspend fun findAll(): Result<List<Card>>

    suspend fun findById(id: String): Result<Card>

    suspend fun save(items: List<Card>): Result<Unit>

    suspend fun update(items: List<Card>): Result<Unit>

    suspend fun delete(items: List<Card>): Result<Unit>
}
