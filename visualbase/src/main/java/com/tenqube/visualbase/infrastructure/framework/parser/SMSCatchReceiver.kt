package com.tenqube.visualbase.infrastructure.framework.parser

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService.Companion.sendIntentService
import com.tenqube.visualbase.infrastructure.framework.parser.util.SMSUtil

class SMSCatchReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            requireNotNull(context)
            requireNotNull(intent)
            if (SMS_RECEIVED_ACTION == intent.action) {
                SMSUtil.parseBundle(intent.extras)?.let {
                    sendIntentService(context, it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}