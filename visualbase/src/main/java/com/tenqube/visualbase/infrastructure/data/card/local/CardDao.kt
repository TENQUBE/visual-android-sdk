package com.tenqube.visualbase.infrastructure.data.card.local

import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cardModel")
    fun getAll(): List<CardModel>

    @Insert
    fun insertAll(vararg cards: CardModel)

    @Update
    fun update(card: CardModel)

    @Delete
    fun delete(card: CardModel)
}