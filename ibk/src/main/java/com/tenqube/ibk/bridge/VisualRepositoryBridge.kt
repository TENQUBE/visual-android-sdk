package com.tenqube.ibk.bridge

import android.webkit.WebView
import com.tenqube.ibk.VisualViewModel
import com.tenqube.ibk.bridge.dto.request.GetTransactionsRequest
import com.tenqube.shared.webview.BridgeBase

class VisualRepositoryBridge(
    webView: WebView,
    private val viewModel: VisualViewModel
): BridgeBase(webView), Bridge.Repository {
    override val bridgeName: String
        get() = "visualRepository"

    override fun getBanks() {
        execute(
            funcName = this@VisualRepositoryBridge::getBanks.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.getBanks()
                }
            })
    }

    override fun getTransactions(params: String?) {
        execute(funcName = this@VisualRepositoryBridge::getTransactions.name,
            params = params,
            classOfT = GetTransactionsRequest::class.java,
            body = {
                it?.let {
                    viewModel.getTransactions(it.data)
                }
            })
    }
}