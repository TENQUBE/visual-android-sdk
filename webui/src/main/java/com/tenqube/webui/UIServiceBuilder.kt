package com.tenqube.webui

import androidx.appcompat.app.AppCompatActivity
import com.tenqube.webui.component.ad.AdService

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
        val adService = AdService(
            activity
        )
        return UiServiceImpl(
            adService,
            activity,
            callback
        )
    }
}
