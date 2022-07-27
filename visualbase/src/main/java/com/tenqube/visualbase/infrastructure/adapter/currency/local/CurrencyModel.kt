package com.tenqube.visualbase.infrastructure.adapter.currency.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "fromCountry")
    val fromCountry: String,
    @ColumnInfo(name = "toCountry")
    val toCountry: String,
    @ColumnInfo(name = "rate")
    val rate: Float,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long
) {
}
