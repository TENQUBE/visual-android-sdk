package com.tenqube.visualbase.domain.parser

import android.annotation.SuppressLint
import android.database.Cursor
import com.tenqube.shared.util.Constants
import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.service.parser.BulkAdapter
import tenqube.parser.model.Transaction
import java.io.Serializable
import java.util.*

interface ParserService {
    suspend fun parseBulk(adapter: BulkAdapter)
    suspend fun parse(sms: SMS): List<ParsedTransaction>
    suspend fun getSmsList(filter: SmsFilter): List<SMS>
    suspend fun getRcsList(filter: SmsFilter): List<SMS>
}

data class SmsFilter(val fromAt: Long, val toAt: Long) {
    fun getQueryCondition(): String {
        return "date >= $fromAt AND date <= $toAt"
    }
}

data class SMS(
    val smsId: Int,
    val fullSms: String,
    val originTel: String,
    val displayTel: String,
    val smsDate: String,
    val smsType: Int,
    val title: String = ""
) : Serializable {

    fun toParser(): tenqube.parser.model.SMS {
        return tenqube.parser.model.SMS(
            smsId,
            fullSms,
            originTel,
            displayTel,
            smsDate,
            smsType,
            title
        )
    }

    companion object {
        @SuppressLint("Range")
        fun from(cursor: Cursor): SMS {
            val smsId = cursor.getInt(cursor.getColumnIndex("_id"))
            val body = cursor.getString(cursor.getColumnIndex("body"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            val date = cursor.getLong(cursor.getColumnIndex("date"))
            return SMS(
                smsId = smsId,
                fullSms = body,
                originTel = address,
                displayTel = address,
                smsDate = Utils.convertDateToDateTimeStr(Date(date)),
                smsType = Constants.SMSType.SMS.ordinal
            )
        }

        fun fromParser(sms: tenqube.parser.model.SMS): SMS {
            return SMS(
                sms.smsId,
                sms.fullSms,
                sms.sender,
                sms.sender,
                sms.smsDate,
                sms.smsType,
                sms.title
            )
        }
    }
}

data class ParsedTransaction(
    val transaction: Transaction
)
