package com.tenqube.visualbase.infrastructure.adapter.notification.local

import android.content.Context
import android.content.res.AssetManager
import com.tenqube.visualbase.domain.notification.NotificationApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class NotificationAppLocalDataSource(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getNotiCatchApps(): List<NotificationApp> = withContext(ioDispatcher) {
        val am: AssetManager = context.assets
        val inStream: InputStream
        val buffer: BufferedReader
        val apps = mutableListOf<NotificationApp>()
        try {
            inStream = am.open("notification_apps.tsv")
            buffer = BufferedReader(InputStreamReader(inStream))
            var line: String? = ""
            var i = 0
            while (buffer.readLine().also { line = it } != null) {
                if (i == 0) {
                    i++
                    continue
                }
                line?.split("\t")?.toTypedArray()?.let {
                    apps.add(
                        NotificationApp(
                            it[0],
                            it[1],
                            it[2],
                        )
                    )
                }
            }
            inStream.close()
            buffer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext apps
    }
}
