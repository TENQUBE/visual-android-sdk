package com.tenqube.ibk.bridge

import android.webkit.WebView
import com.tenqube.ibk.VisualViewModel
import com.tenqube.ibk.bridge.dto.request.GetTransactionsRequest
import com.tenqube.ibk.bridge.dto.request.OpenDeepLinkRequest
import com.tenqube.shared.webview.BridgeBase

class SystemBridge(
    webView: WebView,
    private val viewModel: VisualViewModel
): BridgeBase(webView), Bridge.System {
    override val bridgeName: String
        get() = "visualSystem"

    override fun openNotiSettings() {
        execute(
            funcName = this@SystemBridge::openNotiSettings.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.openNotiSettings()
                }
            })
    }

    override fun openDeepLink(params: String?) {
        execute(
            funcName = this@SystemBridge::openDeepLink.name,
            params = params,
            classOfT = OpenDeepLinkRequest::class.java,
            body = {
                it?.let {
                    viewModel.openDeepLink(it.data)
                }
            })
    }
}