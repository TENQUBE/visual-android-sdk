package com.tenqube.visualbase.infrastructure.framework.parser

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
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
                        Log.i("RCS", "SmsParsingService start $sms")
                        ServiceLocator.provideParserAppService(context = applicationContext)
                            .parse(sms).getOrThrow()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createTestSMS(): SMS {
//        return SMS(
//            0,
//            "07/20 10:33 501-01-0***-${(1..1000).random()} 기업이윤재 스마트폰출금 50,000 잔액2,256,257",
//            "com.kbstar.starpush",
//            "com.kbstar.starpush",
//            "2022-07-28 00:10:10",
//            0,
//            ""
//        )

        return SMS(
            0,
            "MG체크카드(8*0*) 13,000원 승인 07/22 12:13 (MG라이프) 주식",
            "com.smg.mgnoti",
            "com.smg.mgnoti",
            "2022-07-28 00:10:10",
            0,
            ""
        )

//        return SMS(
//            0,
//            "07/22 13:25 137-12-0***68-8 출금 49,000원 잔액 1,300,000원 주식투자",
//            "com.citibank.cardapp",
//            "com.citibank.cardapp",
//            "2022-07-28 00:10:10",
//            0,
//            ""
//        )

//        return SMS(
//            0,
//            "(7*4*)홍*혜님 07/11 17:51/일시불/승인/35,000원/엔씨송파점/누적이용금액 1,275,340원",
//            "com.hanaskcard.app.touchstamp",
//            "com.hanaskcard.app.touchstamp",
//            "2022-07-28 00:10:10",
//            0,
//            ""
//        )
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
