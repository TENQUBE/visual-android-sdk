package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.parser.SmsFilter
import kotlinx.coroutines.runBlocking
import tenqube.parser.OnNetworkResultListener
import tenqube.parser.model.SMS
import tenqube.parser.model.Transaction
import java.util.*

class BulkSmsAdapterImpl(
    private val bulkParserAppService: BulkParserAppService,
    private val callback: BulkCallback
) : BulkAdapter {

    private var smsList: List<SMS> = mutableListOf()

    init {
        callback.onStart()
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
        callback.onProgress(now, total)
    }

    override fun sendToServerTransactions(
        transactions: ArrayList<Transaction>,
        callback: OnNetworkResultListener
    ) = runBlocking {
        bulkParserAppService.saveTransactions(transactions)
    }

    override fun onCompleted() {
        callback.onCompleted()
    }

    override fun onError(resultCode: Int) {
        callback.onError(resultCode)
    }
}

interface BulkCallback {
    fun onStart()
    fun onProgress(now: Int, total: Int)
    fun onCompleted()
    fun onError(code: Int)
}