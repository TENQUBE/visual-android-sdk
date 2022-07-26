package com.tenqube.visualbase.infrastructure.adapter.notification

import com.tenqube.visualbase.domain.notification.dto.NotificationDto
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction
import java.io.Serializable


data class VisualIBKReceiptDto(
    val keyword: String,
    val cardName: String,
    val amount: Int,
    val paymentDate: String,
    val largeCategory: String,
    val mediumCategory: String,
    val smallCategory: String,
    val franchise: String
): Serializable {
    fun toLink(): String {
        return  "v=1&" +
                "dv=1.0&" +
                "keyword=${keyword}&" +
                "cardName=${cardName}&" +
                "amount=${amount}&" +
                "paymentDate=${paymentDate}&" +
                "largeCategory=${largeCategory}&" +
                "mediumCategory=${mediumCategory}&" +
                "smallCategory=${smallCategory}&" +
                "franchise=${franchise}"
    }

    companion object {
        fun from(item: NotificationDto) : VisualIBKReceiptDto {
            return VisualIBKReceiptDto(
                item.transaction.transaction.keyword,
                item.transaction.card.name,
                item.transaction.transaction.spentMoney.toInt(),
                item.transaction.transaction.spentDate,
                item.transaction.category.large,
                item.transaction.category.medium,
                item.transaction.category.small,
                item.transaction.transaction.company.name
            )
        }
    }
}