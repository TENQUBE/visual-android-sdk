package com.tenqube.visualbase.infrastructure.framework.parser.mms

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.text.TextUtils
import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.domain.parser.SMS
import tenqube.transmsparser.constants.Constants
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

object MMSParser {

    @SuppressLint("Range")
    fun parse(context: Context): SMS? {
        var mmsCursor: Cursor? = null
        try {
            val mmsUri = Uri.parse("content://mms/inbox")
            mmsCursor = context.contentResolver
                .query(mmsUri, null, "msg_box = 1", null, "_id desc limit 1")
            if (mmsCursor != null) {
                if (mmsCursor.moveToFirst()) {
                    val id = mmsCursor.getInt(mmsCursor.getColumnIndex("_id"))
                    val date = mmsCursor.getLong(mmsCursor.getColumnIndex("date")) * 1000
                    val tel = getMMSAddress(context, id)
                    val msg = parseMessage(context, id.toString()) ?: return null

                    return SMS(
                        id,
                        msg,
                        tel,
                        tel,
                        Utils.convertDateToDateTimeStr(Date(date)),
                        Constants.SMSType.SMS.ordinal
                    )
                }
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
        } finally {
            mmsCursor?.close()
        }

        return null
    }

    @SuppressLint("Range")
    private fun getMMSAddress(context: Context, id: Int): String {
        val addrSelection = "msg_id=$id"
        var cursor: Cursor? = null
        var address = ""
        val uriStr = "content://mms/$id/addr"
        val uriAddress = Uri.parse(uriStr)
        val columns = arrayOf("address")
        try {
            cursor = context.contentResolver.query(
                uriAddress, columns,
                addrSelection, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val `val` = cursor.getString(cursor.getColumnIndex("address"))
                    if (`val` != null) {
                        address = `val`.replace("-", "")
                        break
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return address
    }

    @SuppressLint("Range")
    private fun parseMessage(context: Context, id: String): String? {
        var cursor: Cursor? = null
        var msg: String? = null
        val uri = Uri.parse("content://mms/part")
        try {
            cursor = context.contentResolver.query(
                uri, null,
                null, null, null
            )
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast) {
                        val mid = cursor.getString(cursor.getColumnIndex("mid"))
                        if (id == mid) {
                            val partId = cursor.getString(cursor.getColumnIndex("_id"))
                            val type = cursor.getString(cursor.getColumnIndex("ct"))
                            if ("text/plain" == type) {
                                val data = cursor.getString(cursor.getColumnIndex("_data"))
                                msg = if (data != null && !TextUtils.isEmpty(data)) {
                                    getMmsText(context, partId)
                                } else {
                                    cursor.getString(cursor.getColumnIndex("text"))
                                }
                            }
                        }
                        cursor.moveToNext()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return msg
    }

    private fun getMmsText(context: Context, id: String): String? {
        val partURI = Uri.parse("content://mms/part/$id")
        var `is`: InputStream? = null
        val sb = StringBuilder()
        try {
            `is` = context.contentResolver.openInputStream(partURI)
            if (`is` != null) {
                val isr = InputStreamReader(`is`, "UTF-8")
                val reader = BufferedReader(isr)
                var temp = reader.readLine()
                while (temp != null) {
                    sb.append(temp)
                    temp = reader.readLine()
                }
                isr.close()
                reader.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                }
            }
        }
        return sb.toString()
    }
}
