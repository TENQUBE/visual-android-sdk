package com.tenqube.visualbase.infrastructure.framework.parser

import android.app.IntentService
import android.content.Intent
import tenqube.parser.model.SMS

class SmsMmsService : IntentService("SmsParsingService") {
    override fun onHandleIntent(intent: Intent?) {
        try {
            if (intent != null) {
                val sms = intent.getSerializableExtra(ARG_SMS) as SMS?
                if (sms != null) {
//                    OneLoader.getInstance(applicationContext)
//                        .doParsing(sms)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val ARG_SMS = "ARG_SMS"
    }
}