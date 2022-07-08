package com.tenqube.webui

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class UIServiceBuilder {
    private lateinit var activity: AppCompatActivity
    private lateinit var webView: WebView

    fun activity(activity: AppCompatActivity): UIServiceBuilder = apply {
        this.activity = activity
    }

    fun webView(webView: WebView): UIServiceBuilder = apply {
        this.webView = webView
    }

    fun build(
    ): UIService {
        return UiServiceImpl(
            activity,
            webView
        )
    }
}
