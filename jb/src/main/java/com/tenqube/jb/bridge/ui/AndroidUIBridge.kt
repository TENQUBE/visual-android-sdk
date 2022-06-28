package com.tenqube.jb.bridge.ui

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.tenqube.jb.bridge.VisualBridge
import com.tenqube.jb.bridge.dto.request.*
import com.tenqube.shared.webview.BridgeBase
import com.tenqube.webui.UIService

class AndroidUIBridge(
    webView: WebView,
    private val uiService: UIService
) : BridgeBase(webView), VisualBridge.UI {

    override val bridgeName: String
        get() = "visualUI"

    @JavascriptInterface
    override fun finish() {
        execute(
            funcName = this@AndroidUIBridge::finish.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                uiService.finish()
        })
    }

    @JavascriptInterface
    override fun onClick() {
        execute(funcName = this@AndroidUIBridge::onClick.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    uiService.onClickSound()
                }
            })
    }

    @JavascriptInterface
    override fun onPageLoaded() {
        execute(funcName = this@AndroidUIBridge::onPageLoaded.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    uiService.onPageLoaded() // 백키 권한을 웹뷰로 전달
                }
            })
    }

    @JavascriptInterface
    override fun setRefreshEnabled(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::setRefreshEnabled.name,
            params = params,
            classOfT = RefreshRequest::class.java,
            body = {
                it?.let {
                    uiService.setRefreshEnabled(it.data.enabled)
                }
            })
    }

    @JavascriptInterface
    override fun showToast(params: String?) {
        execute(funcName = this@AndroidUIBridge::showToast.name,
            params = params,
            classOfT = ShowToastRequest::class.java,
            body = {
                it?.let {
                    uiService.showToast(it.data.message)
                }
            })
    }

    @JavascriptInterface
    override fun showConfirm(params: String?) {
        execute(funcName = this@AndroidUIBridge::showConfirm.name,
            params = params,
            classOfT = ShowConfirmRequest::class.java,
            body = {
                it?.let {
                    uiService.showConfirm(it.data.asDomain())
                }
            })
    }

    @JavascriptInterface
    override fun showSelectBox(params: String?) {
        execute(funcName = this@AndroidUIBridge::showSelectBox.name,
            params = params,
            classOfT = ShowSelectBoxRequest::class.java,
            body = {
                it?.let {
                    uiService.showSelectBox(it.data.asDomain())
                }
            })
    }

    @JavascriptInterface
    override fun openNewView(params: String?) {
        execute(funcName = this@AndroidUIBridge::openNewView.name,
            params = params,
            classOfT = OpenNewViewRequest::class.java,
            body = {
                it?.let {
                    uiService.openNewView(it.data.asDomain())
                }
            })
    }

    @JavascriptInterface
    override fun showDatePicker(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showDatePicker.name,
            params = params,
            classOfT = ShowDatePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.showDatePicker(it.data.asDomain())
                }
            }
        )
    }

    @JavascriptInterface
    override fun showTimePicker(params: String?) {
        execute(funcName = this@AndroidUIBridge::showTimePicker.name,
            params = params,
            classOfT = ShowTimePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.showTimePicker(it.data.asDomain())
                }
            })
    }
}
