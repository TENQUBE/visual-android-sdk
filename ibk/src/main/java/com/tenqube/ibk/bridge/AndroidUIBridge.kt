package com.tenqube.ibk.bridge

import android.webkit.WebView
import com.tenqube.ibk.VisualViewModel
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.shared.webview.BridgeBase

class AndroidUIBridge(
    webView: WebView,
    private val viewModel: VisualViewModel
) : BridgeBase(webView), Bridge.UI {
    override val bridgeName: String
        get() = "visualUI"

    override fun showToast(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showToast.name,
            params = params,
            classOfT = ShowToastRequest::class.java,
            body = {
                it?.let {
                    viewModel.showToast(it.data.message)
                }
            }
        )
    }

    override fun openSelectBox(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openSelectBox.name,
            params = params,
            classOfT = OpenSelectBoxRequest::class.java,
            body = {
                it?.let {
                    viewModel.openSelectBox(it.data)
                }
            }
        )
    }

    override fun showAd(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showAd.name,
            params = params,
            classOfT = ShowAdRequest::class.java,
            body = {
                it?.let {
                    viewModel.showAd(it.data)
                }
            }
        )
    }

    override fun hideAd() {
        execute(
            funcName = this@AndroidUIBridge::finish.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.hideAd()
            }
        )
    }

    override fun openNewView(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openNewView.name,
            params = params,
            classOfT = OpenNewViewRequest::class.java,
            body = {
                it?.let {
                    viewModel.openNewView(it.data)
                }
            }
        )
    }

    override fun finish() {
        execute(
            funcName = this@AndroidUIBridge::finish.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.finish()
            }
        )
    }
}
