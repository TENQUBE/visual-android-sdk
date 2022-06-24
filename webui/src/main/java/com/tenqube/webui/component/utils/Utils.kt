package com.tenqube.webui.component.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val timeDf = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
    val ymdDF = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val fullDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)

    fun getDeviceHeight(activity: Activity): Int {
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(dm)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(dm)
        }
        return dm.heightPixels
    }
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    fun changeColor(container: LinearLayout, color: Int) {
        val background = container.background
        if (background is GradientDrawable) {
            background.setStroke(3, color)
        }
    }
}