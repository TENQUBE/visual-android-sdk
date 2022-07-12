package com.tenqube.visualbase.infrastructure.adapter.currency.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyModel(
    val fromCountry: String,
    val toCountry: String,
    val rate: Float,
    val createdAt: Long
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
