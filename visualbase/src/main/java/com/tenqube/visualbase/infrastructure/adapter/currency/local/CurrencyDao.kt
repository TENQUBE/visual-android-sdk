package com.tenqube.visualbase.infrastructure.adapter.currency.local

import androidx.room.*

@Dao
interface CurrencyDao {

    @Update
    suspend fun update(model: CurrencyModel)

    @Insert
    suspend fun save(model: CurrencyModel)

    @Query("select * from CurrencyModel")
    fun findAll(): List<CurrencyModel>

    @Query("select * from CurrencyModel where fromCountry = :from and toCountry = :to")
    fun findByFromAndTo(from: String, to: String): CurrencyModel?

    @Delete
    fun delete(currency: CurrencyModel)
}
