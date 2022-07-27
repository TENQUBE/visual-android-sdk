package com.tenqube.visualbase.infrastructure.framework.parser.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService.Companion.sendIntentService
import com.tenqube.visualbase.infrastructure.framework.parser.rcs.RcsCatchReceiver

class SMSCatchReceiver() : BroadcastReceiver() {
    var rcsService: RcsService? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("RCS", "onReceive")

        try {
            requireNotNull(context)
            requireNotNull(intent)
            if (SMS_RECEIVED_ACTION == intent.action) {
                SMSParser.parseBundle(intent.extras)?.let {
                    sendIntentService(context, it)
                }
            } else if (RcsCatchReceiver.RCS_RECEIVED_ACTION == intent.action) {
                val msgId = intent.extras?.get("msg_id").toString()
                Log.i("RCS", "msgId $msgId")
                (
                    rcsService ?: RcsService(context).apply {
                        rcsService = this
                    }
                    ).queryRcs(msgId)?.run {
                    sendIntentService(context, this)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
        const val RCS_RECEIVED_ACTION = "com.services.rcs.MESSAGE_RECEIVED"
    }
}
