package com.tenqube.visual_ibk_sdk_android.bridge

import android.webkit.WebView
import com.tenqube.visual_ibk_sdk_android.MainViewModel
import com.tenqube.visual_ibk_sdk_android.bridge.dto.request.*
import com.tenqube.visual_ibk_sdk_android.bridge.dto.request.OpenNewViewRequest

class AndroidUI(
    webView: WebView,
    private val viewModel: MainViewModel
): BridgeBase(webView), Bridge.UI {
    override val bridgeName: String
        get() = "visualUI"

    override fun openNotiSettings() {
        execute(funcName = this@AndroidUI::openNotiSettings.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.openNotiSettings()
                }
            })
    }

    override fun getBanks() {
        execute(funcName = this@AndroidUI::getBanks.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.getBanks()
                }
            })
    }

    override fun openDeepLink(params: String?) {
        execute(funcName = this@AndroidUI::getBanks.name,
            params = params,
            classOfT = OpenDeepLinkRequest::class.java,
            body = {
                it?.let {
                    viewModel.openDeepLink(it.data)
                }
            })
    }

    override fun showToast(params: String?) {
        execute(funcName = this@AndroidUI::showToast.name,
            params = params,
            classOfT = ShowToastRequest::class.java,
            body = {
                it?.let {
                    viewModel.showToast(it.data.message)
                }
            })
    }

    override fun openSelectBox(params: String?) {
        execute(
            funcName = this@AndroidUI::openSelectBox.name,
            params = params,
            classOfT = OpenSelectBoxRequest::class.java,
            body = {
                it?.let {
                    viewModel.openSelectBox(it.data)
                }
            })
    }

    override fun showAd(params: String?) {
        execute(
            funcName = this@AndroidUI::showAd.name,
            params = params,
            classOfT = ShowAdRequest::class.java,
            body = {
                it?.let {
                    viewModel.showAd(it.data)
                }
            })
    }

    override fun hideAd() {
        execute(
            funcName = this@AndroidUI::finish.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.hideAd()
            })
    }

    override fun openNewView(params: String?) {
        execute(
            funcName = this@AndroidUI::openNewView.name,
            params = params,
            classOfT = OpenNewViewRequest::class.java,
            body = {
                it?.let {
                    viewModel.openNewView(it.data)
                }
            })
    }

    override fun finish() {
        execute(
            funcName = this@AndroidUI::finish.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                viewModel.finish()
            })
    }

    override fun getTransactions(params: String?) {
        execute(funcName = this@AndroidUI::getTransactions.name,
            params = params,
            classOfT = GetTransactionsRequest::class.java,
            body = {
                it?.let {
                    viewModel.getTransactions(it.data)
                }
            })
    }
}