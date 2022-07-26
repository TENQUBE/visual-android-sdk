package com.tenqube.visualbase.infrastructure.adapter.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.shared.util.encodeToBase64
import com.tenqube.shared.util.toJson
import com.tenqube.visualbase.domain.notification.NotificationApp
import com.tenqube.visualbase.domain.notification.NotificationService
import com.tenqube.visualbase.domain.notification.dto.NotificationDto
import com.tenqube.visualbase.infrastructure.adapter.notification.local.NotificationAppLocalDataSource

class NotificationServiceImpl(
    private val context: Context,
    private val prefStorage: PrefStorage,
    private val notificationAppLocalDataSource: NotificationAppLocalDataSource
) : NotificationService{
    companion object {
        private const val NOTI_ID = 1000
    }

    override fun show(command: NotificationDto) {
        createNotificationChannel()
        val receipt = VisualIBKReceiptDto.from(command)
        val builder = NotificationCompat.Builder(context, prefStorage.notiChannelId)
            .setSmallIcon(prefStorage.notiIcon)
            .setContentTitle(command.getTitle())
            .setContentText(command.getMsg())
            .setWhen(command.getDate())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createIntent(context, receipt.toJson()))

        with(NotificationManagerCompat.from(context)) {
            notify(NOTI_ID, builder.build())
        }
    }

    override suspend fun getNotifications(): List<NotificationApp> {
        return notificationAppLocalDataSource.getNotiCatchApps()
    }

    private fun createIntent(context: Context, json: String): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("visual://ibk-receipt?link=${json.encodeToBase64()}")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O  &&
            !isExistChannel(notificationManager)) {
            val name = prefStorage.notiChannelName
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(prefStorage.notiChannelId , name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isExistChannel(notificationManager: NotificationManager): Boolean {
        return notificationManager.getNotificationChannel(prefStorage.notiChannelId) != null
    }
}