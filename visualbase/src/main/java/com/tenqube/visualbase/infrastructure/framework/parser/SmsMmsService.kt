package com.tenqube.visualbase.infrastructure.framework.parser

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SmsMmsService : IntentService("SmsParsingService"), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun onHandleIntent(intent: Intent?) = runBlocking {
        try {
            if (intent != null) {
                val sms = intent.getSerializableExtra(ARG_SMS) as SMS?
                if (sms != null) {
                    withContext(Dispatchers.Default) {
                        ServiceLocator.provideParserAppService().parse(sms)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val ARG_SMS = "ARG_SMS"

        fun sendIntentService(context: Context, sms: SMS) {
            val startIntent = Intent(context, SmsMmsService::class.java)
            startIntent.putExtra(ARG_SMS, sms)
            context.startService(startIntent)
        }
    }
}
