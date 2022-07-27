package com.tenqube.visualbase.infrastructure.adapter.currency.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CurrencyDao {

    @Update
    fun update(model: CurrencyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(model: CurrencyModel)

    @Query("select * from currency")
    fun findAll(): List<CurrencyModel>

    @Query("select * from currency where fromCountry = :from and toCountry = :to")
    fun findByFromAndTo(from: String, to: String): CurrencyModel?

    @Delete
    fun delete(currency: CurrencyModel)
}
