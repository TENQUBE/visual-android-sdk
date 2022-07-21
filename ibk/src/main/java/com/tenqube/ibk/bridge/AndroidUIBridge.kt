package com.tenqube.ibk.bridge

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tenqube.ibk.VisualViewModel
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.shared.webview.BridgeBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AndroidUIBridge(
    lifecycleOwner: LifecycleOwner,
    webView: WebView,
    private val viewModel: VisualViewModel
) : BridgeBase(lifecycleOwner, webView), Bridge.UI {
    override val bridgeName: String
        get() = "visualSDK"

    @JavascriptInterface
    override fun showToast(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showToast.name,
            params = params,
            classOfT = ShowToastRequest::class.java,
            body = {
                it?.let {
                    viewModel.showToast(it.message)
                }
            }
        )
    }

    @JavascriptInterface
    override fun openSelectBox(params: String?) {
        val funcName = this@AndroidUIBridge::openSelectBox.name
        execute(
            funcName = funcName,
            params = params,
            classOfT = OpenSelectBoxRequest::class.java,
            body = {
                it?.let {
                    viewModel.openSelectBox(it) {
                        launch {
                            onSuccess(funcName, it)
                        }
                    }
                }
            }
        )
    }

    @JavascriptInterface
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

    @JavascriptInterface
    override fun hideAd() {
        execute(
            funcName = this@AndroidUIBridge::hideAd.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.hideAd()
            }
        )
    }

    @JavascriptInterface
    override fun openNewView(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openNewView.name,
            params = params,
            classOfT = OpenNewViewRequest::class.java,
            body = {
                it?.let {
                    viewModel.openNewView(it)
                }
            }
        )
    }

    @JavascriptInterface
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

    @JavascriptInterface
    override fun openNotiSettings() {
        execute(
            funcName = this@AndroidUIBridge::openNotiSettings.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.openNotiSettings()
                }
            }
        )
    }

    @JavascriptInterface
    override fun openDeepLink(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openDeepLink.name,
            params = params,
            classOfT = OpenDeepLinkRequest::class.java,
            body = {
                it?.let {
                    viewModel.openDeepLink(it)
                }
            }
        )
    }

    @JavascriptInterface
    override fun getBanks() {
        val funcName = this@AndroidUIBridge::getBanks.name
        execute(
            funcName = funcName,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.getBanks()
            }
        )

        launch(Dispatchers.Main) {
            viewModel.banks.observe(lifecycleOwner, Observer {
                GlobalScope.launch {
                    onSuccess(funcName, it)
                }
            })
        }
    }

    @JavascriptInterface
    override fun getTransactions(params: String?) {
        val funcName = this@AndroidUIBridge::getTransactions.name
        execute(
            funcName = funcName,
            params = params,
            classOfT = GetTransactionsRequest::class.java,
            body = {
                it?.let {
                    viewModel.getTransactions(it)
                }
            }
        )
        launch(Dispatchers.Main) {
            viewModel.transactions.observe(lifecycleOwner, Observer {
                GlobalScope.launch {
                    onSuccess(funcName, it)
                }
            })
        }
    }
}
