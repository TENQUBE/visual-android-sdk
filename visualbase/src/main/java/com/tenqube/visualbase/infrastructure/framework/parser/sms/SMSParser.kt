package com.tenqube.visualbase.infrastructure.framework.parser.sms

import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.tenqube.visualbase.domain.parser.SMS
import tenqube.transmsparser.constants.Constants
import java.text.SimpleDateFormat
import java.util.*

object SMSParser {

    @RequiresApi(Build.VERSION_CODES.M)
    fun parseBundle(bundle: Bundle?): SMS? {
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

    @RequiresApi(Build.VERSION_CODES.M)
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getSmsMessage(
        bundle: Bundle,
        item: Any
    ): SmsMessage? {
        val format = bundle.getString("format")
        return SmsMessage.createFromPdu(item as ByteArray, format)
    }

    @RequiresApi(Build.VERSION_CODES.M)
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

    @RequiresApi(Build.VERSION_CODES.M)
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

    @RequiresApi(Build.VERSION_CODES.M)
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
}
