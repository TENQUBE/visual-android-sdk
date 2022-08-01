package com.tenqube.ibkreceipt

import android.content.Context
import android.service.notification.StatusBarNotification
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService
import com.tenqube.visualbase.infrastructure.framework.parser.noti.NotiParser

object ParseNotiService {
    fun parseNoti(context: Context, sbn: StatusBarNotification, testPkgName: String? = null) {
        SmsMmsService.sendIntentService(
            context,
            NotiParser.parseSbn(context, sbn, testPkgName)
        )
    }
}