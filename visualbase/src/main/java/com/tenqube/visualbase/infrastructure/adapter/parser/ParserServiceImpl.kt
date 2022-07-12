package com.tenqube.visualbase.infrastructure.adapter.parser

import android.content.Context
import android.net.Uri
import com.tenqube.shared.util.getValue
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.parser.SmsFilter
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.adapter.parser.rule.ParsingRuleService
import tenqube.parser.model.ResultCode

class ParserServiceImpl(
    private val context: Context,
    private val parserService: tenqube.parser.core.ParserService,
    private val parsingRuleService: ParsingRuleService,
    private val rcsService: RcsService
) : ParserService {

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
        val parsingRule = parsingRuleService.getParsingRule()
        parserService.syncParsingRule(parsingRule.parsingRule)
    }

    private suspend fun syncWhenNoSender() {
        parsingRuleService.getParsingRuleWhenNoSender()?.let {
            parserService.syncParsingRule(it.parsingRule)
        }
    }

    override suspend fun getSmsList(filter: SmsFilter): List<SMS> {
        val results = mutableListOf<SMS>()
        try {
            val uri = Uri.parse("content://sms/inbox")
            val cursor = context.contentResolver.query(
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
        }

        return results
    }

    override suspend fun getRcsList(filter: SmsFilter): List<SMS> {
        return rcsService.queryRcs(filter.fromAt, filter.toAt).getValue()
    }
}
