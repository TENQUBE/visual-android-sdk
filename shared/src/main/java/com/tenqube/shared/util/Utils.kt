package com.tenqube.shared.util

import android.content.Context
import android.util.Log
import com.tenqube.shared.error.FormatNotMatchedException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    var isDebug = false
    const val TAG = "VisualSDK"

    fun logD(className: Class<Any>, msg: String) {
        if(isDebug) {
            Log.d(TAG, "${className.simpleName} $msg")
        }
    }

    fun convertDateToDateTimeStr(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertDateStrToDate(str: String): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(str)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertDateToDateStr(str: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(str)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertDateTimeStrToDate(str: String): Date {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(str)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertDateTimeStrToCalendar(str: String): Calendar {
        return Calendar.getInstance().apply {  time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(str) }
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertDateStrToCalendar(str: String): Calendar {
        return Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(str) }
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToDateStr(date: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertTimeStrToCalendar(str: String): Calendar {
        return Calendar.getInstance().apply { time = SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(str) }
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToTimeStr(date: Calendar): String {
        return SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToMonthStr(date: Calendar): String {
        return SimpleDateFormat("yyyy-MM", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToDateTimeStr(date: Calendar): String {

        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToSyncDateStr(date: Calendar): String {

        return SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertTimeStrToDate(str: String): Date {
        return SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(str)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String

        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getCurrentWeekMonday(): Calendar {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).dayOfWeek()
        calendar.add(Calendar.DATE, -(dayOfWeek - 1)) // 월요일로 만듦니다.
        return calendar
    }

}