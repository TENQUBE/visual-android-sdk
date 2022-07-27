package com.tenqube.visualbase.infrastructure.data.card.local

import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAll(): List<CardModel>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: String): CardModel?

    @Insert
    fun insertAll(cards: CardModel): Long

    @Update
    fun update(card: CardModel)

    @Delete
    fun delete(card: CardModel)
}
