package com.tenqube.visualbase.infrastructure.framework.parser.mms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService

class MMSCatchReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val runn = Runnable {
                try {
                    val action = intent.action
                    if (action != null) {
                        if (MMS_ACTION == action) {
                            MMSParser.parse(context)?.let {
                                SmsMmsService.sendIntentService(context, it)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val handler = Handler()
            handler.postDelayed(runn, 1000) // 시간이 너무 짧으면 못 가져오는게 있더라
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val MMS_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED"
    }
}