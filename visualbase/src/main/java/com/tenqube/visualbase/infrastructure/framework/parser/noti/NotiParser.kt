package com.tenqube.visualbase.infrastructure.framework.parser.noti

import android.app.Notification
import android.content.Context
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.tenqube.visualbase.domain.parser.SMS
import tenqube.transmsparser.model.NotiRequest

object NotiParser {

    fun parseSbn(context: Context, sbn: StatusBarNotification): SMS {
        val packageName = sbn.packageName
        val notification = sbn.notification
        val titleCS = notification.extras.getCharSequence(Notification.EXTRA_TITLE)
        val contentCS = notification.extras.getCharSequence(Notification.EXTRA_TEXT)
        val bigCS = notification.extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT)

        val title = titleCS?.toString() ?: ""
        val content = contentCS?.toString() ?: ""
        val bigContent = bigCS?.toString() ?: ""

        return if (isTest(content)) {
            parseNotiRequest(content)
        } else {
            val sms = NotiRequest(
                packageName,
                title,
                content,
                bigContent,
                notification.`when`
            ).toSMS(context)
            SMS.fromParser(sms)
        }
    }

    private fun isTest(content: String): Boolean {
        return content.startsWith("`")
    }

    private fun parseNotiRequest(content: String): SMS {
        val items = content.split(";")
        val sender = items[0].replace("`", "")
        return SMS(
            smsId = 0,
            fullSms = items[2],
            originTel = sender,
            displayTel = sender,
            "2022-07-07 00:00:00",
            2
        )
    }
}
