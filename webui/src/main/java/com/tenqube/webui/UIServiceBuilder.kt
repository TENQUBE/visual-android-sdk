package com.tenqube.webui

import androidx.appcompat.app.AppCompatActivity

class UIServiceBuilder {
    private lateinit var activity: AppCompatActivity

    fun activity(activity: AppCompatActivity): UIServiceBuilder = apply {
        this.activity = activity
    }

    fun build(): UIService {
        return UiServiceImpl(
            activity
        )
    }
}
