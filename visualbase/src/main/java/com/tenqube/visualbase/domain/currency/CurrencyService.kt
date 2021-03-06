package com.tenqube.visualbase.domain.currency

interface CurrencyService {
    suspend fun exchange(request: CurrencyRequest): Double
    suspend fun prepopulate()
}

data class CurrencyRequest(
    val from: String,
    val to: String = "KRW",
    val amount: Double
) {
    fun isKorea(): Boolean {
        return (from.isEmpty() || from == "none" || from == "KRW") && to == "KRW"
    }
}
