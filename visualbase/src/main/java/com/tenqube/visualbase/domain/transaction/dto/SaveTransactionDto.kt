package com.tenqube.visualbase.domain.transaction.dto

import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.transaction.Company

data class SaveTransactionDto(
    val id: String,
    val categoryCode: String,
    val cardName: String,
    val cardType: Int,
    val cardSubType: Int,
    val company: Company,
    val spentDate: String,
    val finishDate: String,
    val spentMoney: Double,
    val oriSpentMoney: Double,
    val installmentCnt: Int,
    val keyword: String,
    val currency: String,
    val dwType: Int,
    val memo: String,
    val sms: SMS,
    val regId: Int,
    val classCode: String
) {
    fun getUniqueCardKey(): String {
        return "${cardName}${cardType}${cardSubType}}"
    }
}
