package com.tenqube.jb.infrastructure.framework.widget

import android.app.Activity
import android.webkit.WebSettings
import android.webkit.WebView

class WebViewManager(private val activity: Activity) {

    fun setupWebViewSettings(webView: WebView) {
        webView.settings.apply {
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

    fun setupBridges(webView: WebView) {
//        uiService = WidgetUIService(activity, webView)
//        with(AndroidUIBridge(webView, uiService)) {
//            webView.addJavascriptInterface(this, this.bridgeName)
//        }
    }
}