package com.tenqube.shared.webview

import android.view.View
import android.webkit.*
import java.util.*

data class WebViewParam(
    val webView: WebView
)

class WebViewManager(
    private val param: WebViewParam
) {

    fun setupWebView() {
        with(param.webView) {
            setupWebViewSettings(this)
            setupWebChromeClient(this)
            setupWebViewClient(this)
        }
    }

    private fun setupWebViewSettings(webView: WebView) {
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

    private fun setupWebViewClient(webView: WebView) {
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                webView.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    private fun setupWebChromeClient(webView: WebView) {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(cm: ConsoleMessage?): Boolean {
                val message = (cm?.message() ?: "").lowercase(Locale.getDefault())
                return super.onConsoleMessage(cm)
            }
        }
    }
}
