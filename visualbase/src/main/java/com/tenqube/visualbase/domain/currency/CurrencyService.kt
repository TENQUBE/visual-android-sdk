package com.tenqube.visualbase.domain.currency

interface CurrencyService {
    suspend fun exchange(request: CurrencyRequest): Double
}

data class CurrencyRequest(
    val from: String,
    val to: String = "KRW",
    val amount: Double
)