package com.tenqube.shared.util

import android.util.Log
import com.google.gson.Gson
import com.tenqube.shared.error.FormatNotMatchedException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    var isDebug = false
    const val TAG = "VisualSDK"
    private val THREE_COMMA_FORMAT = DecimalFormat("###,###")

    fun logD(className: Class<Any>, msg: String) {
//        if (isDebug) {
            Log.d(TAG, "${className.simpleName} $msg")
//        }
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
        return Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(str) }
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

    fun convertCalendarToDateTimeStr(date: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun convertCalendarToSyncDateStr(date: Calendar): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(date.time)
            ?: throw FormatNotMatchedException("Date Format Not Matched")
    }

    fun getCurrentWeekMonday(): Calendar {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).dayOfWeek()
        calendar.add(Calendar.DATE, -(dayOfWeek - 1)) // 월요일로 만듦니다.
        return calendar
    }

    fun <T> fromJson(param: String?, classOfT: Class<T>?): T {
        val gson = Gson()
        return gson.fromJson(param, classOfT)
    }

    fun String.toDateTime(): Long? {
        val fullDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        return fullDF.parse(this)?.time
    }

    fun threeComma(value: Double): String {
        return THREE_COMMA_FORMAT.format(value.toLong()) + "원"
    }

    fun installment(amount: Int): String {
        return when(amount) {
            1 -> "일시불"
            else -> "${amount}개월"
        }
    }
}
