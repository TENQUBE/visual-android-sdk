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
import com.tenqube.visualbase.domain.notification.NotificationService
import com.tenqube.visualbase.domain.notification.dto.NotificationDto

class NotificationServiceImpl(
    private val context: Context,
    private val prefStorage: PrefStorage) : NotificationService{
    override fun show(command: NotificationDto) {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(context, prefStorage.notiChannelId)
            .setSmallIcon(prefStorage.notiIcon)
            .setContentTitle(command.getTitle())
            .setContentText(command.getMsg())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createIntent(context))

        with(NotificationManagerCompat.from(context)) {
            notify(1000, builder.build())
        }
    }

    private fun createIntent(context: Context): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("visual://ibk-receipt")).apply {
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