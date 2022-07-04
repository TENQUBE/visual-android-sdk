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
        return listOf()
    }

    private fun initCursor() {
        try {
            val uri = Uri.parse("content://sms/inbox")
            val cursor = context.contentResolver.query(uri, null, getWhere(), null, "date asc")
            cursor?.moveToFirst()
        } catch (e: Exception) {
        }
    }

    private fun getWhere(): String {
        return ""
    }
}