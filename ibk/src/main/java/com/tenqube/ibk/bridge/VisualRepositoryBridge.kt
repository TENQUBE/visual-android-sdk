package com.tenqube.ibk.bridge

import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tenqube.ibk.VisualViewModel
import com.tenqube.ibk.bridge.dto.request.GetTransactionsRequest
import com.tenqube.shared.webview.BridgeBase
import kotlinx.coroutines.launch

class VisualRepositoryBridge(
    lifecycleOwner: LifecycleOwner,
    webView: WebView,
    private val viewModel: VisualViewModel
) : BridgeBase(lifecycleOwner, webView), Bridge.Repository {
    override val bridgeName: String
        get() = "visualRepository"

    override fun getBanks() {
        val funcName = this@VisualRepositoryBridge::getBanks.name
        execute(
            funcName = funcName,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    viewModel.getBanks()
                }
            }
        )

        viewModel.banks.observe(lifecycleOwner, Observer {
            launch {
                onSuccess(funcName, it)
            }
        })
    }

    override fun getTransactions(params: String?) {
        val funcName = this@VisualRepositoryBridge::getTransactions.name
        execute(
            funcName = funcName,
            params = params,
            classOfT = GetTransactionsRequest::class.java,
            body = {
                it?.let {
                    viewModel.getTransactions(it.data)
                }
            }
        )

        viewModel.transactions.observe(lifecycleOwner, Observer {
            launch {
                onSuccess(funcName, it)
            }
        })
    }
}
