package com.tenqube.visualbase.infrastructure.adapter.parser.rcs

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import com.tenqube.shared.util.Result
import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.infrastructure.framework.parser.rcs.RcsParser

class RcsService(private val context: Context) {

    private val LG_URI = Uri.parse("content://com.lge.ims.rcsim.database.provider.message")
    private val SAMSUNG_URI = Uri.parse("content://im/chat")

    suspend fun queryRcs(from: Long, to: Long): Result<List<SMS>> {
        return Result.Success(
            when {
                isSamsung() -> {
                    queryRcsForSAMSUNG(from, to)
                }
                isLg() -> {
                    queryRcsForLG(from, to)
                }
                else -> {
                    emptyList()
                }
            }
        )
    }

    private fun isSamsung(): Boolean {
        return Build.BRAND.contains("samsung")
    }

    private fun isLg(): Boolean {
        return Build.BRAND.contains("lg")
    }

    private fun queryRcsForSAMSUNG(from: Long, to: Long): List<SMS> {
        var c: Cursor? = null
        val results = ArrayList<SMS>()
        try {
            val arg = arrayOfNulls<String>(2)
            arg[0] = from.toString()
            arg[1] = to.toString()
            c = context.contentResolver.query(
                SAMSUNG_URI, null,
                "type = 1 and date > ? and date <= ?", arg, "date asc"
            )
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast) {
                        val messageId = c.getString(0)
                        queryRcs(messageId)?.let {
                            results.add(it)
                        }
                        c.moveToNext()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            Utils.logD(javaClass, "RcsServiceImpl queryRcsForSAMSUNG c $e")
        } finally {
            c?.close()
        }
        return results.toList()
    }

    fun queryRcs(messageId: String): SMS? {
        return if (context.contentResolver == null) {
            null
        } else {
            var c: Cursor? = null
            var sms: SMS? = null
            try {
                c = context.contentResolver.query(Uri.parse("content://im/rcs_read_im_msgid/$messageId"), null, null, null, null)
                if (c != null) {

                    if (c.moveToFirst()) {
                        while (!c.isAfterLast) {
                            sms = RcsParser.parseRcs(c)
                            c.moveToNext()
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
            } finally {
                c?.close()
            }
            sms
        }
    }

    private fun queryRcsForLG(from: Long, to: Long): List<SMS> {
        var c: Cursor? = null
        val results = ArrayList<SMS>()
        try {
            // momt_mode = 0 전달받은 정보만 처리합니다 .
            c = context.contentResolver.query(LG_URI, null, "date > $from and date <= $to", null, "date asc")
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast) {
                        RcsParser.parseRcs(c)?.let {
                            results.add(it)
                        }
                        c.moveToNext()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }

        return results.toList()
    }
}
