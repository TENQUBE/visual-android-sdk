package com.tenqube.webui

import androidx.appcompat.app.AppCompatActivity

class UIServiceBuilder {
    private lateinit var activity: AppCompatActivity
    private lateinit var callback: (Boolean) -> Unit

    fun activity(activity: AppCompatActivity): UIServiceBuilder = apply {
        this.activity = activity
    }

    fun refreshCallback(callback: (Boolean) -> Unit) = apply {
        this.callback = callback
    }

    fun build(): UIService {
        return UiServiceImpl(
            activity,
            callback
        )
    }
}
