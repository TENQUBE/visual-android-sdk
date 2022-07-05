package com.tenqube.visualbase.infrastructure.adapter.parser

import android.content.Context
import android.net.Uri
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.parser.SmsFilter

class ParserServiceImpl(
    private val context: Context,
    private val parserService: tenqube.parser.core.ParserService,
) : ParserService {

    override suspend fun parse(sms: SMS): List<ParsedTransaction> {
        return listOf()
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