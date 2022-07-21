package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.parser.SmsFilter
import kotlinx.coroutines.runBlocking
import tenqube.parser.OnNetworkResultListener
import tenqube.parser.model.FinancialProduct
import tenqube.parser.model.SMS
import tenqube.parser.model.Transaction
import java.util.*
import kotlin.collections.ArrayList

class BulkSmsAdapterImpl(
    private val bulkParserAppService: BulkParserAppService,
    private val callback: BulkCallback
) {

    private var smsList: List<SMS> = mutableListOf()

    init {
        callback.onStart()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -4)
        val smsFilter = SmsFilter(calendar.timeInMillis, System.currentTimeMillis())
        smsList = bulkParserAppService.getSmsList(smsFilter)
    }


    fun getSmsCount(): Int {
        return smsList.size
    }

    fun getSmsAt(n: Int): SMS {
        return smsList[n]
    }

    fun onProgress(now: Int, total: Int) {
        callback.onProgress(now, total)
    }

    fun sendToServerTransactions(
        transactions: ArrayList<Transaction>,
        products: ArrayList<FinancialProduct>,
        callback: OnNetworkResultListener
    ) = runBlocking {
        bulkParserAppService.saveTransactions(transactions)
    }

    fun onCompleted() {
        callback.onCompleted()
    }

    fun onError(resultCode: Int) {
        callback.onError(resultCode)
    }
}

interface BulkCallback {
    fun onStart()
    fun onProgress(now: Int, total: Int)
    fun onCompleted()
    fun onError(code: Int)
}