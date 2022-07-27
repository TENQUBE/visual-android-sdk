package com.tenqube.visualbase.infrastructure.data.transaction.remote

import com.tenqube.shared.util.toInt
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.Serializable

interface TransactionApiService {
    @POST
    suspend fun saveTransactions(
        @Url url: String,
        @HeaderMap header: Map<String, String>,
        @Body request: TransactionRequest
    )
}

data class TransactionRequest(
    val transactions: List<TransactionDto>
) : Serializable {
    companion object {
        fun from(items: List<JoinedTransaction>): TransactionRequest {
            return TransactionRequest(
                items.mapNotNull {
                    TransactionDto(
                        it.transaction.id,
                        it.card.name,
                        "",
                        it.card.type,
                        it.card.subType,
                        it.transaction.spentMoney,
                        it.transaction.oriSpentMoney,
                        it.transaction.spentDate,
                        it.transaction.finishDate,
                        it.transaction.keyword,
                        it.transaction.keyword,
                        it.transaction.installmentCnt,
                        it.transaction.dwType,
                        it.transaction.currency,
                        it.card.balance,
                        it.transaction.sms.originTel,
                        it.transaction.sms.fullSms,
                        it.transaction.sms.smsDate,
                        it.transaction.sms.smsType,
                        it.transaction.isOffset.toInt(),
                        0,
                        it.transaction.memo,
                        it.transaction.lat,
                        it.transaction.lng,
                        it.category.code.toInt(),
                        it.transaction.company.id.toInt(),
                        it.transaction.classCode,
                        it.transaction.regId,
                        0,
                        0,
                        0,
                        0
                    )
                }
            )
        }
    }
}

data class TransactionDto(
    val identifier: String,
    val cardName: String,
    val cardNum: String,
    val cardType: Int,
    val cardSubType: Int,
    val spentMoney: Double,
    val oriSpentMoney: Double,
    val spentDate: String,
    val finishDate: String,
    val oriKeyword: String,
    val searchKeyword: String,
    val installmentCount: Int,
    val dwType: Int,
    val currency: String,
    val balance: Double,
    val sender: String,
    val fullSms: String,
    val smsDate: String,
    val smsType: Int,
    val isOffset: Int,
    val isDuplicate: Int,
    val memo: String,
    val spentLatitude: Double,
    val spentLongitude: Double,
    val categoryCode: Int,
    val companyId: Int,
    val classCode: String,
    val regId: Int,
    val isCustom: Int,
    val isUserUpdate: Int,
    val isUpdateAll: Int,
    val isDeleted: Int
) : Serializable

data class CurrencyResponse(val rate: Float)
