package com.tenqube.visualbase.service.parser

import android.util.Log
import com.tenqube.visualbase.domain.parser.SmsFilter
import kotlinx.coroutines.runBlocking
import tenqube.transmsparser.OnNetworkResultListener
import tenqube.transmsparser.model.SMS
import tenqube.transmsparser.model.Transaction
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
        Log.i("RCS", "smsSize ${smsList.size}")
        return smsList.size
    }

    override fun getSmsAt(n: Int): SMS {
        return smsList[n].apply {
            Log.i("RCS", "sms ${this.fullSms}")
        }
    }

    override fun onProgress(now: Int, total: Int) {
        Log.i("RCS", "now ${now}/ total ${total}")
        callback.onProgress(now, total)
    }

    override fun sendToServerTransactions(
        transactions: ArrayList<Transaction>,
        callback: OnNetworkResultListener
    ) = runBlocking {
        transactions.forEach {
            Log.i("RCS", "tran ${it.cardName}")
        }
        bulkParserAppService.saveTransactions(transactions)
        callback.onResult(true)
    }

    override fun onCompleted() {
        Log.i("RCS", "onCompleted")
        callback.onCompleted()
    }

    override fun onError(resultCode: Int) {
        Log.i("RCS", "onError")
        callback.onError(resultCode)
    }
}

interface BulkCallback {
    fun onStart()
    fun onProgress(now: Int, total: Int)
    fun onCompleted()
    fun onError(code: Int)
}