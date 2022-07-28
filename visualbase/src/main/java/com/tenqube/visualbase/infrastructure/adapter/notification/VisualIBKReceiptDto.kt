package com.tenqube.visualbase.infrastructure.adapter.notification

import android.net.Uri
import com.tenqube.visualbase.domain.notification.dto.NotificationDto
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
) : Serializable {
    fun toLink(): String {
        return "v=1&" +
            "dv=1.0&" +
            "keyword=$keyword&" +
            "cardName=$cardName&" +
            "amount=$amount&" +
            "paymentDate=$paymentDate&" +
            "largeCategory=$largeCategory&" +
            "mediumCategory=$mediumCategory&" +
            "smallCategory=$smallCategory&" +
            "franchise=$franchise"
    }

    companion object {

        fun from(uri: Uri): VisualIBKReceiptDto {
            val keyword = uri.getQueryParameter("keyword") ?: throw Exception("query string is empty")
            val cardName = uri.getQueryParameter("cardName") ?: throw Exception("query string is empty")
            val amount = uri.getQueryParameter("amount") ?: throw Exception("query string is empty")
            val paymentDate = uri.getQueryParameter("paymentDate") ?: throw Exception("query string is empty")
            val largeCategory = uri.getQueryParameter("largeCategory") ?: throw Exception("query string is empty")
            val mediumCategory = uri.getQueryParameter("mediumCategory") ?: throw Exception("query string is empty")
            val smallCategory = uri.getQueryParameter("smallCategory") ?: throw Exception("query string is empty")
            val franchise = uri.getQueryParameter("franchise") ?: throw Exception("query string is empty")

            return VisualIBKReceiptDto(
                keyword,
                cardName,
                amount.toInt(),
                paymentDate,
                largeCategory,
                mediumCategory,
                smallCategory,
                franchise
            )
        }

        fun from(item: NotificationDto): VisualIBKReceiptDto {
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
