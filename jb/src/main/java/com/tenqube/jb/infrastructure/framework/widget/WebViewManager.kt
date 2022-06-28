package com.tenqube.jb.infrastructure.framework.widget

import android.app.Activity
import android.webkit.WebSettings
import android.webkit.WebView

data class WebViewParam(
    val activity: Activity,
    val webView: WebView
)

class WebViewManager(
    private val param: WebViewParam
) {

    fun setupWebViewSettings() {
        param.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            textZoom = 100
            cacheMode = WebSettings.LOAD_NO_CACHE
        }

        WebView.setWebContentsDebuggingEnabled(true)
    }
}