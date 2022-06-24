package com.tenqube.reward.util

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber
import kotlin.math.max

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

@Throws(JsonSyntaxException::class)
fun <T> fromJson(param: String, classOfT: Class<T>): T {
    return Gson().fromJson(param, classOfT)
}

fun Long.time(second: Int): Long {
    return max(0, second * 1000 - (System.currentTimeMillis() - this))
}

fun Long.elapsedLog(clazz: Class<Any>, msg: String) {
    Log.i(clazz.simpleName, msg)

    Timber.tag(
        clazz.simpleName).d(
        "$msg / 경과시간: ${(System.currentTimeMillis() - this) / 1000}s ${System.currentTimeMillis() - this} ms "
    )
}
