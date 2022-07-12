package com.tenqube.jb.bridge

import android.webkit.JavascriptInterface

interface VisualBridge {

    interface UI {
        val bridgeName: String

        @JavascriptInterface
        fun finish()

        @JavascriptInterface
        fun onClick()

        @JavascriptInterface
        fun onPageLoaded()

        @JavascriptInterface
        fun setRefreshEnabled(params: String?)

        @JavascriptInterface
        fun showToast(params: String?)

        @JavascriptInterface
        fun openConfirm(params: String?)

        @JavascriptInterface
        fun openSelectBox(params: String?)

        @JavascriptInterface
        fun openNewView(params: String?)

        @JavascriptInterface
        fun openDeepLink(params: String?)

        @JavascriptInterface
        fun openDatePicker(params: String?)

        @JavascriptInterface
        fun openTimePicker(params: String?)

        @JavascriptInterface
        fun openNotiSettings()

        @JavascriptInterface
        fun showAd(params: String?)

        @JavascriptInterface
        fun hideAd(params: String?)
    }
}
