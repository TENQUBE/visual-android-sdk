package com.tenqube.visualbase.domain.transaction.command

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
    val sms: SMS
)
