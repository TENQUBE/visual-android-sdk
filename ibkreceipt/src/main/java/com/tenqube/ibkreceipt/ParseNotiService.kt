package com.tenqube.ibkreceipt

import android.content.Context
import android.service.notification.StatusBarNotification
import com.tenqube.visualbase.infrastructure.framework.parser.SmsParsingService
import com.tenqube.visualbase.infrastructure.framework.parser.noti.NotiParser

object ParseNotiService {
    fun parseNoti(context: Context, sbn: StatusBarNotification, testPkgName: String? = null) {
        SmsParsingService.sendIntentService(
            context,
            NotiParser.parseSbn(context, sbn, testPkgName)
        )
    }
}