package com.tenqube.visualbase.infrastructure.adapter.parser

import android.content.Context
import android.net.Uri
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.parser.SmsFilter
import com.tenqube.visualbase.infrastructure.adapter.parser.rule.ParsingRuleService
import com.tenqube.visualbase.service.resource.ResourceAppService
import tenqube.parser.model.ResultCode

class ParserServiceImpl(
    private val context: Context,
    private val parserService: tenqube.parser.core.ParserService,
    private val parsingRuleService: ParsingRuleService
) : ParserService {

    override suspend fun parse(sms: SMS): List<ParsedTransaction> {
        val results = mutableListOf<ParsedTransaction>()

        val result = parserService.parse(sms.toParser())
        when(result.resultCode) {
            ResultCode.NEED_TO_SYNC_PARSING_RULE,
            ResultCode.NEED_TO_SEND_TO_SERVER ->
                parsingRuleService.sync()
            ResultCode.NEED_TO_SYNC_PARSING_RULE_NO_SENDER ->
                parsingRuleService.syncWhenNoSender()
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

    override suspend fun getSmsList(filter: SmsFilter): List<SMS> {
        val results = mutableListOf<SMS>()
        try {
            val uri = Uri.parse("content://sms/inbox")
            val cursor = context.contentResolver.query(
                uri,
                null,
                filter.getQueryCondition(),
                null,
                "date asc")
            cursor?.let {
                if(it.moveToFirst()) {
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
}