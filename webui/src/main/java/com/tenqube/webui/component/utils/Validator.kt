package com.tenqube.webui.component.utils

import android.text.TextUtils
import com.tenqube.webui.exception.ParameterException
import java.lang.Exception

object Validator {
    @Throws(ParameterException::class)
    fun isNotEmpty(value: String) {
        if (TextUtils.isEmpty(value)) throw ParameterException("$value is empty")
    }

    @Throws(ParameterException::class)
    fun isDate(at: String) {
        var isValid = !TextUtils.isEmpty(at) && at.length == 19
        Utils.fullDF.isLenient = false
        try {
            Utils.fullDF.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterException("Check date :" + at + "at.length" + at.length)
    }

    @Throws(ParameterException::class)
    fun isTime(at: String) {
        var isValid = !TextUtils.isEmpty(at) && at.length == 8
        Utils.timeDf.isLenient = false
        try {
            Utils.timeDf.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterException("Check date :$at")
    }

    @Throws(ParameterException::class)
    fun isYMD(at: String) {
        var isValid = !TextUtils.isEmpty(at) && at.length == 10
        Utils.ymdDF.isLenient = false
        try {
            Utils.ymdDF.parse(at)
        } catch (e: Exception) {
            isValid = false
        }
        if (!isValid) throw ParameterException("Check date :$at")
    }

    @Throws(ParameterException::class)
    fun len(value: String, len: Int) {
        if (TextUtils.isEmpty(value) || value.length != len) throw ParameterException("$value is invalid value, (valid len : $len)")
    }

    @Throws(ParameterException::class)
    fun isStr(value: String, len: Int) {
        if (TextUtils.isEmpty(value) || value.length > len) throw ParameterException("$value is invalid value")
    }

    @Throws(ParameterException::class)
    fun isStrWithNull(value: String?, len: Int) {
        if (value == null || value.length > len) throw ParameterException("$value is invalid value")
    }

    @Throws(ParameterException::class)
    fun gt(value: Int, target: Int) {
        if (value < target) throw ParameterException("value: $value/target:$target")
    }

    @Throws(ParameterException::class)
    fun lt(value: Int, target: Int) {
        if (value > target) throw ParameterException("value: $value/target:$target")
    }

    @Throws(ParameterException::class)
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
        if (!isValid) throw ParameterException(
            "value: " + value + " / value in (" + TextUtils.join(
                ",",
                rules
            ) + ""
        )
    }

    @Throws(ParameterException::class)
    fun `in`(value: Int, vararg rules: Int) {
        var isValid = false
        for (rule in rules) {
            if (value == rule) {
                isValid = true
                break
            }
        }
        if (!isValid) throw ParameterException(
            "value: $value/ value in (" + TextUtils.join(
                ",",
                rules.toList()
            ) + ""
        )
    }

    @Throws(ParameterException::class)
    fun notZero(value: Int) {
        if (value == 0) throw ParameterException("value: $value value is zero")
    }

    @Throws(ParameterException::class)
    fun notNull(`object`: Any?) {
        if (`object` == null) throw ParameterException("value is null")
    }

    @Throws(ParameterException::class)
    fun notEmpty(values: Array<Int?>?) {
        if (values == null || values.size == 0) throw ParameterException("values is empty")
    }
}
