package com.tenqube.webui

import androidx.appcompat.app.AppCompatActivity
import com.tenqube.webui.exception.ParameterException

class UIServiceBuilder {

    private var activity: AppCompatActivity? = null

    fun with(
        activity: AppCompatActivity
    ): UIServiceBuilder = apply {
        this.activity = activity
    }

    fun build(): UIService {
        requireNotNull(activity) {
            throw ParameterException("activity is null")
        }
        return UiServiceImpl(
            activity!!
        )
    }
}
