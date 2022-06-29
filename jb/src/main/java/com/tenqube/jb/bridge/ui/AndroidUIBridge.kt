package com.tenqube.jb.bridge.ui

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.tenqube.jb.bridge.VisualBridge
import com.tenqube.jb.bridge.dto.request.*
import com.tenqube.jb.bridge.dto.request.OpenNewViewRequest
import com.tenqube.jb.bridge.dto.request.RefreshRequest
import com.tenqube.jb.bridge.dto.request.ShowConfirmRequest
import com.tenqube.shared.webview.BridgeBase
import com.tenqube.webui.UIService
import com.tenqube.webui.component.dialog.DialogCallback
import com.tenqube.webui.dto.ShowDatePicker
import com.tenqube.webui.dto.ShowDialog
import com.tenqube.webui.dto.ShowSelectBox
import com.tenqube.webui.dto.ShowTimePicker

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
            }
        )
    }

    @JavascriptInterface
    override fun onClick() {
        execute(
            funcName = this@AndroidUIBridge::onClick.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                    uiService.onClickSound()
                }
            }
        )
    }

    @JavascriptInterface
    override fun onPageLoaded() {
        execute(
            funcName = this@AndroidUIBridge::onPageLoaded.name,
            params = null,
            classOfT = Any::class.java,
            body = {
                it?.let {
                }
            }
        )
    }

    @JavascriptInterface
    override fun setRefreshEnabled(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::setRefreshEnabled.name,
            params = params,
            classOfT = RefreshRequest::class.java,
            body = {
                it?.let {
//                    uiService.setRefreshEnabled(it.data.enabled)
                }
            }
        )
    }

    @JavascriptInterface
    override fun showToast(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showToast.name,
            params = params,
            classOfT = ShowToastRequest::class.java,
            body = {
                it?.let {
                    uiService.showToast(it.data.message)
                }
            }
        )
    }

    @JavascriptInterface
    override fun showConfirm(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showConfirm.name,
            params = params,
            classOfT = ShowConfirmRequest::class.java,
            body = {
                it?.let {
                    uiService.showDialog(
                        ShowDialog(
                            request = it.data.asDomain(),
                            object : DialogCallback {
                                override fun onClickPositiveButton() {
                                }

                                override fun onCLickNegativeButton() {
                                }
                            }
                        )
                    )
                }
            }
        )
    }

    @JavascriptInterface
    override fun showSelectBox(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showSelectBox.name,
            params = params,
            classOfT = ShowSelectBoxRequest::class.java,
            body = {
                it?.let {
                    uiService.showSelectBox(
                        ShowSelectBox(request = it.data.asDomain()) { selectBox ->
                        }
                    )
                }
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
                    uiService.openNewView(it.data.asDomain())
                }
            }
        )
    }

    @JavascriptInterface
    override fun showDatePicker(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showDatePicker.name,
            params = params,
            classOfT = ShowDatePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.showDatePicker(
                        ShowDatePicker(request = it.data.asDomain()) {
                        }
                    )
                }
            }
        )
    }

    @JavascriptInterface
    override fun showTimePicker(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::showTimePicker.name,
            params = params,
            classOfT = ShowTimePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.showTimePicker(
                        ShowTimePicker(request = it.data.asDomain()) {
                        }
                    )
                }
            }
        )
    }
}
