package com.tenqube.visualbase.infrastructure.data.card.local

import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cardModel")
    suspend fun getAll(): List<CardModel>

    @Query("SELECT * FROM cardModel WHERE id = :id")
    suspend fun getById(id: String): CardModel?

    @Insert
    suspend fun insertAll(cards: CardModel)

    @Update
    suspend fun update(card: CardModel)

    @Delete
    suspend fun delete(card: CardModel)
}
