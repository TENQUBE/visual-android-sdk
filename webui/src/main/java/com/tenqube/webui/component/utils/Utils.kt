package com.tenqube.webui.component.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val timeDf = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
    val ymdDF = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val fullDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

    val DECIMAL_FORMAT = DecimalFormat("00")


    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun changeColor(container: LinearLayout, color: Int) {
        val background = container.background
        if (background is GradientDrawable) {
            background.setStroke(3, color)
        }
    }

    fun toCalendarYMD(dateStr: String?): Calendar? {
        val calendar = Calendar.getInstance()
        try {
            dateStr?.let {
                ymdDF.parse(it)?.let { date ->
                    calendar.time = date
                }
            }
        } catch (e: ParseException) {
        }
        return calendar
    }

    fun getYMD(date: Calendar): String {
        val year = date[Calendar.YEAR]
        val month = date[Calendar.MONTH] + 1
        val day = date[Calendar.DATE]
        return "$year-" + DECIMAL_FORMAT.format(
            month.toLong()
        ) + "-" + DECIMAL_FORMAT.format(day.toLong())
    }

    fun getHMS(date: Calendar): String {
        val hour = date[Calendar.HOUR_OF_DAY]
        val minute = date[Calendar.MINUTE]
        val second = date[Calendar.SECOND]
        return "" + DECIMAL_FORMAT.format(hour.toLong()) + ":" + DECIMAL_FORMAT.format(
            minute.toLong()
        ) + ":" + DECIMAL_FORMAT.format(second.toLong())
    }

    fun toCalendarHMS(time: String?): Calendar? {
        val calendar = Calendar.getInstance()
        try {
            time?.let {
                timeDf.parse(time)?.let { date ->
                    calendar.time = date
                }
            }
        } catch (e: ParseException) {
        }
        return calendar
    }

    fun getApplicationName(context: Context): String {
        return ""
    }
}
