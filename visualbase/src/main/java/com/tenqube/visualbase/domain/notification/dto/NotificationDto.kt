package com.tenqube.visualbase.domain.notification.dto

import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction

data class NotificationDto(
    val transaction: JoinedTransaction
) {
    fun getMsg(): String {
        return "${Utils.threeComma(transaction.transaction.spentMoney)} " +
                "${Utils.dwType(transaction.card.type, transaction.transaction.dwType)} | " +
                "${transaction.card.name}(${Utils.installment(transaction.transaction.installmentCnt)})"
    }

    fun getTitle(): String {
        return "i-ONE 영수증 - ${transaction.transaction.keyword}"
    }

    fun getDate(): Long {
        return Utils.convertDateTimeStrToCalendar(transaction.transaction.spentDate).timeInMillis
    }
}
