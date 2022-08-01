package com.tenqube.visualibk.noti

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
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
        ParseNotiService.parseNoti(applicationContext, sbn) // 영수증
        SmsManager.parseNoti(applicationContext, sbn) // 가계부
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
    }
}
