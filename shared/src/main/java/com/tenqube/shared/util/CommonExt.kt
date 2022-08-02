package com.tenqube.shared.util

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.lang.Exception
import java.util.*
import kotlin.math.max

fun String.decode(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun ByteArray.encodeToString(): String {
    return try {
        Base64.encodeToString(this, Base64.DEFAULT)
    } catch (e: Exception) {
        this.toString()
    }
}

inline fun <T> invokeFuncWithLoggingTimeElapsed(
    prefix: String,
    function: () -> T
): T {
    val startTime = System.currentTimeMillis()
    val result: T = function.invoke()

    if (Utils.isDebug)
        Log.d("VisualSDK", "$prefix : ${System.currentTimeMillis() - startTime} ms")

    return result
}

fun Boolean.toInt(): Int {
    return if (this) {
        1
    } else {
        0
    }
}

fun Long.elapsedLog(clazz: Class<Any>, msg: String) {
    Utils.logD(
        clazz,
        "$msg / 경과시간: ${(System.currentTimeMillis() - this) / 1000}s ${System.currentTimeMillis() - this} ms "
    )
}

fun Date.toCal(): Calendar {
    return Calendar.getInstance().apply {
        time = this@toCal
    }
}

fun Long.time(second: Int): Long {
    return max(0, second * 1000 - (System.currentTimeMillis() - this))
}

@Throws(JsonSyntaxException::class)
fun <T> String.fromJson(classOfT: Class<T>): T {

    return Gson().fromJson(this, classOfT)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun Int.toLcode(): Int {

    return when {
        this > 100000 -> {
            this / 10000
        }
        this > 1000 -> {
            return this / 100
        }
        else -> {
            return this
        }
    }
}

fun Int.toFullCode(): Int {
    return when {
        this < 100 -> {
            "${this}1010".toInt()
        }
        this < 10000 -> {
            "${this}10".toInt()
        }
        else -> {
            this
        }
    }
}

fun Int.toMcode(): Int {

    return if (this > 100000) {
        this / 100
    } else {
        this
    }
}

fun Int.dayOfWeek(): Int { // 1(월) 2 3 4 5 6 7(일)
    return if (this == 1) {
        7
    } else {
        this - 1
    }
}

fun String.toGroupByMonth(): String {
    return this.substring(0, 7) // 2020-10
}

fun String.toGroupByDate(): String {
    return this.substring(0, 10) // 2020-10-10
}

fun String.toHour(): Int {
    return this.substring(11, 13).toInt() //
}

fun String.toGroupByWeek(currentWeekMonday: Long): String { // 미랠

    val tranTime = Utils.convertDateTimeStrToDate(this).time

    val diff = (currentWeekMonday - tranTime) // 미래를 모두 이번주로 보는 현상이 발생
    val diffDays = diff / (24 * 60 * 60 * 1000) / 7
    return if (diff < 0) {
        diffDays
    } else {
        diffDays + 1
    }.toString()
}

fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.encodeToByteArray(), Base64.NO_WRAP)
}

fun String.decodeBase64(): String {
    return String(Base64.decode(this, Base64.NO_WRAP), Charsets.UTF_8)
}
