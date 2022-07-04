package com.tenqube.visualbase.domain.parser

import tenqube.parser.model.Transaction
import java.io.Serializable

interface ParserService {
    suspend fun parse(sms: SMS): List<ParsedTransaction>

    suspend fun getSmsList(filter: SmsFilter): List<SMS>
}

data class SmsFilter(val fromAt: Long, val toAt: Long)

data class SMS(
    val smsId: Int,
    val fullSms: String,
    val originTel: String,
    val displayTel: String,
    val smsDate: String,
    val smsType: Int
) : Serializable

data class ParsedTransaction(
    val transaction: Transaction
)