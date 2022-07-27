package com.tenqube.visualbase.infrastructure.adapter.parser

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.tenqube.shared.util.getValue
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.parser.SmsFilter
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.adapter.parser.rule.ParsingRuleService
import com.tenqube.visualbase.service.parser.BulkAdapter
import tenqube.transmsparser.BulkSmsAdapter
import tenqube.transmsparser.OnNetworkResultListener
import tenqube.transmsparser.model.FinancialProduct
import tenqube.transmsparser.model.ResultCode
import tenqube.transmsparser.model.Transaction
import java.util.ArrayList

class ParserServiceImpl(
    private val context: Context,
    private val parserService: tenqube.transmsparser.core.ParserService,
    private val parsingRuleService: ParsingRuleService,
    private val rcsService: RcsService
) : ParserService {

    override suspend fun parseBulk(adapter: BulkAdapter) {
        sync()
        parserService.parseBulk(object : BulkSmsAdapter {
            override fun getSmsCount(): Int {
                return adapter.getSmsCount()
            }

            override fun getSmsAt(n: Int): tenqube.transmsparser.model.SMS {
                return adapter.getSmsAt(n)
            }

            override fun onProgress(now: Int, total: Int) {
                adapter.onProgress(now, total)
            }

            override fun sendToServerTransactions(
                transactions: ArrayList<Transaction>,
                products: ArrayList<FinancialProduct>,
                callback: OnNetworkResultListener
            ) {
                adapter.sendToServerTransactions(transactions, callback)
            }

            override fun onCompleted() {
                adapter.onCompleted()
            }

            override fun onError(resultCode: Int) {
                adapter.onError(resultCode)
            }
        })
    }

    override suspend fun parse(sms: SMS): List<ParsedTransaction> {
        val results = mutableListOf<ParsedTransaction>()

        val result = parserService.parse(sms.toParser())
        when (result.resultCode) {
            ResultCode.NEED_TO_SYNC_PARSING_RULE,
            ResultCode.NEED_TO_SEND_TO_SERVER ->
                sync()
            ResultCode.NEED_TO_SYNC_PARSING_RULE_NO_SENDER ->
                syncWhenNoSender()
            ResultCode.SEND_TO_SERVER -> {
                results.addAll(
                    result.transactions.map {
                        ParsedTransaction(it)
                    }
                )
            }
        }

        return results
    }

    private suspend fun sync() {
        parsingRuleService.getParsingRule()?.let {
            parserService.syncParsingRule(it.parsingRule)
        }
    }

    private suspend fun syncWhenNoSender() {
        parsingRuleService.getParsingRuleWhenNoSender()?.let {
            parserService.syncParsingRule(it.parsingRule)
        }
    }

    override suspend fun getSmsList(filter: SmsFilter): List<SMS> {
        val results = mutableListOf<SMS>()
        var cursor: Cursor? = null
        try {
            val uri = Uri.parse("content://sms/inbox")
            cursor = context.contentResolver.query(
                uri,
                null,
                filter.getQueryCondition(),
                null,
                "date asc"
            )
            cursor?.let {
                if (it.moveToFirst()) {
                    while (!it.isAfterLast) {
                        results.add(SMS.from(it))
                        it.moveToNext()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return results
    }

    override suspend fun getRcsList(filter: SmsFilter): List<SMS> {
        return rcsService.queryRcs(filter.fromAt, filter.toAt).getValue()
    }
}
