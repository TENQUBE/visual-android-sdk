package com.tenqube.visualbase.domain.notification.dto

import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction

data class NotificationDto(
    val transaction: JoinedTransaction
) {
    fun getMsg(): String {
        return "${Utils.threeComma(transaction.transaction.spentMoney)} 결제 | " +
                "${transaction.card.name}(${Utils.installment(transaction.transaction.installmentCnt)})"
    }

    fun getTitle(): String {
        return ""
    }

    fun getDate(): Long {
        return 0
    }
}
