package com.tenqube.ibk.bridge.dto.response

data class TransactionsResponse(
    val transactions: List<TransactionDto>
)

data class TransactionDto(
    val id: String,
    val largeCategory: String,
    val mediumCategory: String,
    val smallCategory: String,
    val franchise: String,
    val cardName: String,
    val amount: Int,
    val installmentPeriod: Int,
    val dwType: Int,
    val currency: String,
    val paymentDate: String,
    val keyword: String
)
