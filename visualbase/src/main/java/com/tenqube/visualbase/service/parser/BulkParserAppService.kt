package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.SmsFilter
import kotlinx.coroutines.runBlocking
import tenqube.transmsparser.OnNetworkResultListener
import tenqube.transmsparser.model.SMS
import tenqube.transmsparser.model.Transaction

class BulkParserAppService(
    private val parserAppService: ParserAppService
) {

    suspend fun start(adapter: BulkAdapter) {
        parserAppService.parseBulk(adapter)
    }

    fun getSmsList(filter: SmsFilter): List<SMS> = runBlocking {
        return@runBlocking parserAppService.getSmsList(filter).map {
            SMS(
                it.smsId,
                it.fullSms,
                it.originTel,
                it.displayTel,
                it.smsDate,
                it.smsType
            )
        }
    }

    suspend fun saveTransactions(transactions: ArrayList<Transaction>) {
        parserAppService.saveTransactions(
            transactions.map {
                ParsedTransaction(it)
            }
        )
    }
}

interface BulkAdapter {
    fun getSmsCount(): Int
    fun getSmsAt(n: Int): SMS
    fun onProgress(now: Int, total: Int)
    fun sendToServerTransactions(
        transactions: ArrayList<Transaction>,
        callback: OnNetworkResultListener
    )
    fun onCompleted()
    fun onError(resultCode: Int)
}
