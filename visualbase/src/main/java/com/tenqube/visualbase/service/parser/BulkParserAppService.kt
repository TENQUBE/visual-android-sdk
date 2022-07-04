package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.SmsFilter
import kotlinx.coroutines.runBlocking
import tenqube.parser.BulkSmsAdapter
import tenqube.parser.OnNetworkResultListener
import tenqube.parser.model.FinancialProduct
import tenqube.parser.model.SMS
import tenqube.parser.model.Transaction
import java.util.*
import kotlin.collections.ArrayList

class BulkSmsAdapterImpl(
    private val bulkParserAppService: BulkParserAppService
) : BulkSmsAdapter {

    private val smsList: List<SMS>

    init {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -4)
        val smsFilter = SmsFilter(calendar.timeInMillis, System.currentTimeMillis())
        smsList = bulkParserAppService.getSmsList(smsFilter)
    }

    override fun getSmsCount(): Int {
        return smsList.size
    }

    override fun getSmsAt(n: Int): SMS {
        return smsList[n]
    }

    override fun onProgress(now: Int, total: Int) {
    }

    override fun sendToServerTransactions(
        transactions: ArrayList<Transaction>,
        products: ArrayList<FinancialProduct>,
        callback: OnNetworkResultListener
    ) = runBlocking {
        bulkParserAppService.saveTransactions(transactions)
    }

    override fun onCompleted() {

    }

    override fun onError(resultCode: Int) {
    }
}

class BulkParserAppService(
    private val parserAppService: ParserAppService,
) {
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
        parserAppService.saveTransactions(transactions.map {
            ParsedTransaction(it)
        })
    }
}