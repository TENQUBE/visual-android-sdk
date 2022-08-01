package com.tenqube.visualibk.noti

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.tenqube.ibkreceipt.ParseNotiService
import com.tenqube.visual_third.parser.catcher.noti.SmsManager

class NotiCatchService : NotificationListenerService() {

    override fun onBind(mIntent: Intent): IBinder? {
        return super.onBind(mIntent)
    }

    override fun onUnbind(mIntent: Intent): Boolean {
        return super.onUnbind(mIntent)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        SmsManager.parseNoti(applicationContext, sbn, null) // 가계부
        val runn = Runnable {
            try {
                ParseNotiService.parseNoti(applicationContext, sbn, null) // 영수증
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val handler = Handler()
        handler.postDelayed(runn, 500)
   }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }
}
