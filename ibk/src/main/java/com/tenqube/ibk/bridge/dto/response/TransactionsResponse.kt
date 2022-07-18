package com.tenqube.ibk.bridge.dto.response

import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction

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
) {
    companion object {
        fun fromDomain(
            item: JoinedTransaction
        ): TransactionDto {
            return TransactionDto(
                item.transaction.id,
                item.category.large,
                item.category.medium,
                item.category.small,
                franchise = item.transaction.company.name,
                cardName = item.card.displayName,
                amount = item.transaction.spentMoney.toInt(),
                installmentPeriod = item.transaction.installmentCnt,
                dwType = item.transaction.dwType,
                currency = item.transaction.currency,
                paymentDate = item.transaction.spentDate,
                keyword = item.transaction.keyword
            )
        }
    }
}
