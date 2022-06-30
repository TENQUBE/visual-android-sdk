package com.tenqube.visualbase.infrastructure.framework.parser.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.text.TextUtils
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService
import tenqube.parser.constants.Constants
import java.text.SimpleDateFormat
import java.util.*

object SMSUtil {

    fun parseSms(bundle: Bundle?): SMS? {
        return bundle?.let {
            val items = parsePdus(it)
            SMS(
                0,
                parseFullSms(bundle, items),
                parseOriginTel(bundle, items),
                parseDisplayTel(bundle, items),
                parseSmsDate(bundle, items),
                Constants.SMSType.SMS.ordinal
            )
        }
    }

    private fun parsePdus(bundle: Bundle): Array<Any> {
        val data = bundle["pdus"]
        if (data != null) {
            if (data is Array<*>) {
                val pdusObj = bundle["pdus"] as Array<Any>?
                if (pdusObj != null) {
                    return pdusObj
                }
            }
        }

        return arrayOf()
    }

    private fun parseFullSms(bundle: Bundle, items: Array<Any>): String {
        var originMsg = ""
        var displayMsg = ""
        for (item in items) {
            getSmsMessage(bundle, item)?.let {
                originMsg += it.messageBody
                displayMsg += it.displayMessageBody
            }
        }

        return selectMsg(originMsg, displayMsg)
    }

    private fun selectMsg(originMsg: String, displayMsg: String): String {
        if (TextUtils.isEmpty(originMsg)) {
            return displayMsg
        }
        if (TextUtils.isEmpty(displayMsg)) {
            return originMsg
        }
        val msg: String = if (originMsg == displayMsg) {
            originMsg
        } else {
            displayMsg
        }

        return msg
    }

    private fun getSmsMessage(
        bundle: Bundle,
        item: Any
    ): SmsMessage? {
        val format = bundle.getString("format")
        return SmsMessage.createFromPdu(item as ByteArray, format)
    }

    private fun parseOriginTel(bundle: Bundle, items: Array<Any>): String {
        var originTel = ""
        for (item in items) {
            getSmsMessage(bundle, item)?.let {
                it.originatingAddress?.let { origin ->
                    originTel = origin
                }
            }
        }

        return originTel
    }

    private fun parseDisplayTel(bundle: Bundle, items: Array<Any>): String {
        var displayTel = ""
        for (item in items) {
            getSmsMessage(bundle, item)?.let {
                it.displayOriginatingAddress?.let { display ->
                    displayTel = display
                }
            }
        }

        return displayTel
    }

    private fun parseSmsDate(bundle: Bundle, items: Array<Any>): String {
        var date = 0L
        for (item in items) {
            getSmsMessage(bundle, item)?.let {
                date = it.timestampMillis
            }
        }

        return toDateFormatStr(date)
    }

    private fun toDateFormatStr(time: Long): String {
        val date = Date()
        date.time = time
        val fullDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        return fullDF.format(date)
    }

    fun sendIntentService(context: Context, sms: SMS) {
        val startIntent = Intent(context, SmsMmsService::class.java)
        startIntent.putExtra(SmsMmsService.ARG_SMS, sms)
        context.startService(startIntent)
    }
}