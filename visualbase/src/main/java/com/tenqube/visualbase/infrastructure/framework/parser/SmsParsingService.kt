package com.tenqube.visualbase.infrastructure.framework.parser

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SmsParsingService : IntentService("SmsParsingService"), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun onHandleIntent(intent: Intent?) = runBlocking {
        Log.i("RCS", "SmsParsingService $intent")
        try {
            if (intent != null) {
                val sms = intent.getSerializableExtra(ARG_SMS) as SMS?
                if (sms != null) {
                    withContext(Dispatchers.Default) {
                        ServiceLocator.provideParserAppService(context = applicationContext)
                            .parse(sms).getOrThrow()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val ARG_SMS = "ARG_SMS"

        fun sendIntentService(context: Context, sms: SMS) = runBlocking {
            try {

                ServiceLocator.provideParserAppService(context = context)
                    .parse(sms).getOrThrow()

//                val startIntent = Intent(context, SmsParsingService::class.java)
//                startIntent.putExtra(ARG_SMS, sms)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    context.startService(startIntent)
//                } else {
//                    context.startService(startIntent)
//                }
            } catch (e: Exception) {
            }
        }
    }
}
