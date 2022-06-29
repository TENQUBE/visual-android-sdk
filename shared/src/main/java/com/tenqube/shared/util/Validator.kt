package com.tenqube.shared.util

import android.text.TextUtils
import com.tenqube.shared.error.ParameterError
import java.text.SimpleDateFormat
import java.util.*

object Validator {

    private val timeDf =
        SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    private val ymdDF =
        SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    private val fullDF =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

    @Throws(ParameterError::class)
    fun isDate(at: String?) {
        var isValid = !at.isNullOrEmpty() && at.length == 19
        fullDF.isLenient = false
        try {
            fullDF.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterError("Check date :$at")
    }

    @Throws(ParameterError::class)
    fun isTime(at: String?) {
        var isValid = !at.isNullOrEmpty() && at.length == 8
        timeDf.isLenient = false
        try {
            timeDf.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterError("Check date :$at")
    }

    @Throws(ParameterError::class)
    fun isYMD(at: String?) {
        var isValid = !at.isNullOrEmpty() && at.length == 10
        ymdDF.isLenient = false
        try {
            ymdDF.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterError("Check date :$at")
    }

    @Throws(ParameterError::class)
    fun len(value: String?, len: Int) {
        if (value.isNullOrEmpty() || value.length != len) throw ParameterError(
            "$value is invalid value, (valid len : $len)"
        )
    }

    @Throws(ParameterError::class)
    fun isStr(value: String, len: Int) {
        if (value.isEmpty() || value.length > len) throw ParameterError(
            "$value is invalid value"
        )
    }

    @Throws(ParameterError::class)
    fun isStr(value: String) {
        if (value.isEmpty()) throw ParameterError(
            "$value is invalid value"
        )
    }

    @Throws(ParameterError::class)
    fun isStrWithNull(value: String?, len: Int) {
        if (value.isNullOrEmpty() || value.length > len) throw ParameterError(
            "$value is invalid value"
        )
    }

    @Throws(ParameterError::class)
    fun isStrWithNull(value: String?) {
        if (value.isNullOrEmpty()) throw ParameterError(
            "$value is empty"
        )
    }

    @Throws(ParameterError::class)
    fun gt(value: Int, target: Int) {
        if (value < target) throw ParameterError(
            "value: $value/target:$target"
        )
    }

    @Throws(ParameterError::class)
    fun lt(value: Int, target: Int) {
        if (value > target) throw ParameterError(
            "value: $value/target:$target"
        )
    }

    @Throws(ParameterError::class)
    fun `in`(value: String, vararg rules: String) {
        var isValid = false
        if (!TextUtils.isEmpty(value)) {
            for (rule in rules) {
                if (value == rule) {
                    isValid = true
                    break
                }
            }
        }
        if (!isValid) throw ParameterError(
            "value: $value / value in (" + TextUtils.join(
                ",",
                rules
            ) + ""
        )
    }

    @Throws(ParameterError::class)
    fun `in`(value: Int, vararg rules: Int) {
        var isValid = false
        for (rule in rules) {
            if (value == rule) {
                isValid = true
                break
            }
        }
        if (!isValid) throw ParameterError(
            "value: $value/ value in (" + TextUtils.join(
                ",",
                rules.asList()
            ) + ""
        )
    }

    @Throws(ParameterError::class)
    fun notZero(value: Int) {
        if (value == 0) throw ParameterError("value: $value value is zero")
    }

    @Throws(ParameterError::class)
    fun notNull(`object`: Any?) {
        if (`object` == null) throw ParameterError(
            "value is null"
        )
    }

    @Throws(ParameterError::class)
    fun <T> notEmpty(values: List<T>?) {
        if (values.isNullOrEmpty()) throw ParameterError(
            "values is empty"
        )
    }
}
