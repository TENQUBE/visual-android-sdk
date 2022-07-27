package com.tenqube.visualbase.infrastructure.framework.parser.rcs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.framework.parser.SmsMmsService.Companion.sendIntentService

class RcsCatchReceiver() : BroadcastReceiver() {
    var rcsService: RcsService? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            requireNotNull(context)
            requireNotNull(intent)
            if (RCS_RECEIVED_ACTION == intent.action) {
                val msgId = intent.extras?.get("msg_id").toString()
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
        const val RCS_RECEIVED_ACTION = "com.services.rcs.MESSAGE_RECEIVED"
    }
}
