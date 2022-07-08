package com.tenqube.jb.bridge.ui

import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.tenqube.jb.bridge.VisualBridge
import com.tenqube.jb.bridge.dto.request.*
import com.tenqube.jb.bridge.dto.request.OpenNewViewRequest
import com.tenqube.jb.bridge.dto.request.RefreshRequest
import com.tenqube.jb.bridge.dto.request.OpenConfirmRequest
import com.tenqube.jb.bridge.dto.response.WebResult
import com.tenqube.shared.webview.BridgeBase
import com.tenqube.webui.UIService
import com.tenqube.webui.component.dialog.DialogCallback
import com.tenqube.webui.dto.OpenDatePicker
import com.tenqube.webui.dto.ShowDialog
import com.tenqube.webui.dto.OpenSelectBox
import com.tenqube.webui.dto.OpenTimePicker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AndroidUIBridge(
    private val webView: WebView,
    private val uiService: UIService
) : BridgeBase(webView), VisualBridge.UI {

    override val bridgeName: String
        get() = "visualSDK"

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
//                    uiService.setRefreshEnabled(it.enabled)
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
                    uiService.showToast(it.message)
                }
            }
        )
    }

    @JavascriptInterface
    override fun openConfirm(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openConfirm.name,
            params = params,
            classOfT = OpenConfirmRequest::class.java,
            body = {
                it?.let {
                    uiService.showDialog(
                        ShowDialog(
                            request = it.asDomain(),
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
    override fun openSelectBox(params: String?) {
        val funcName = this@AndroidUIBridge::openSelectBox.name
        execute(
            funcName = funcName,
            params = params,
            classOfT = OpenSelectBoxRequest::class.java,
            body = {
                it?.let {
                    uiService.openSelectBox(
                        OpenSelectBox(request = it.asDomain()) { selectBox ->
                            GlobalScope.launch {
                                onSuccess(funcName, WebResult(selectBox))
                            }
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
                    uiService.openNewView(it.asDomain())
                }
            }
        )
    }

    override fun openDeepLink(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openDeepLink.name,
            params = params,
            classOfT = OpenDeepLinkRequest::class.java,
            body = {
                it?.let {
                    uiService.openNewView(it.asDomain())
                }
            }
        )
    }

    @JavascriptInterface
    override fun openDatePicker(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openDatePicker.name,
            params = params,
            classOfT = OpenDatePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.openDatePicker(
                        OpenDatePicker(request = it.asDomain()) {
                        }
                    )
                }
            }
        )
    }

    @JavascriptInterface
    override fun openTimePicker(params: String?) {
        execute(
            funcName = this@AndroidUIBridge::openTimePicker.name,
            params = params,
            classOfT = OpenTimePickerRequest::class.java,
            body = {
                it?.let {
                    uiService.openTimePicker(
                        OpenTimePicker(request = it.asDomain()) {
                        }
                    )
                }
            }
        )
    }

    override fun openNotiSettings(params: String?) {
        Toast.makeText(webView.context, "openNotiSettings $params", Toast.LENGTH_SHORT).show()
    }

    override fun showAd(params: String?) {
        Toast.makeText(webView.context, "showAd $params", Toast.LENGTH_SHORT).show()
    }

    override fun hideAd(params: String?) {
        Toast.makeText(webView.context, "hideAd $params", Toast.LENGTH_SHORT).show()
    }
}
