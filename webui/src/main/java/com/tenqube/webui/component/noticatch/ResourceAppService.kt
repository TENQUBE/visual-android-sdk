package com.tenqube.webui.component.noticatch

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ResourceAppService(
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
            var line = ""
            var i = 0
            while (buffer.readLine().also { line = it } != null) {
                if (i == 0) {
                    i++
                    continue
                }
                val colums = line.split("\t").toTypedArray()
                apps.add(
                    NotificationApp(
                        colums[1], // name
                        colums[2], // image
                    )
                )
            }
            inStream.close()
            buffer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext apps
    }
}

data class NotificationApp(val name: String, val image: String)
