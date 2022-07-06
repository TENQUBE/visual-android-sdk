package com.tenqube.visualbase.infrastructure.framework.parser.rcs

import android.annotation.SuppressLint
import android.database.Cursor
import com.tenqube.shared.util.Utils
import com.tenqube.visualbase.domain.parser.SMS
import org.json.JSONException
import org.json.JSONObject
import tenqube.parser.constants.Constants
import java.util.*

object RcsParser {

    @SuppressLint("Range")
    fun parseRcs(c: Cursor): SMS? {
        return try {
            val id = c.getInt(c.getColumnIndex("_id"))
            val address = c.getString(c.getColumnIndex("address"))
            val date = c.getLong(c.getColumnIndex("date"))
            val bodyJson = c.getString(c.getColumnIndex("body"))
            val fullSMS: String = getDisplayText(bodyJson)

            if(checkParams(id, address, date, fullSMS)) {
                return null
            }

            SMS(
                id,
                fullSMS,
                address,
                address,
                Utils.convertDateToDateTimeStr(Date(date)),
                Constants.SMSType.SMS.ordinal
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun checkParams(
        id: Int,
        address: String,
        date: Long,
        fullSMS: String
    ) = id == 0 || address.isEmpty() || date == 0L || fullSMS.isEmpty()

    private fun getDisplayText(jsonString: String): String {
        return try {
            val rootObject = JSONObject(jsonString)
            val collector = StringBuilder()
            val layoutObject = rootObject.getJSONObject("layout")
            collectDisplayText(layoutObject, collector, "")
            collector.toString()
        } catch (e: JSONException) {
            jsonString
        }
    }

    @Throws(JSONException::class)
    private fun collectDisplayText(
        widgetObject: JSONObject,
        collector: StringBuilder,
        delimiter: String
    ) {
        when (widgetObject.getString("widget")) {
            "TextView" -> {
                val text = widgetObject.getString("text")
                if (text.isNotEmpty()) {
                    collector.append(delimiter)
                    collector.append(text)
                }
            }
            "LinearLayout" -> {
                val children = widgetObject.getJSONArray("children")
                val length = children.length()
                var i = 0
                while (i < length) {
                    val jsonObject = children.getJSONObject(i)
                    collectDisplayText(jsonObject, collector, delimiter)
                    i++
                }
            }
        }
    }
}