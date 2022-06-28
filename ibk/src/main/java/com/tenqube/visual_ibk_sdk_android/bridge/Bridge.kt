package com.tenqube.visual_ibk_sdk_android.bridge

import android.webkit.JavascriptInterface

interface Bridge {

    interface UI {
        val bridgeName: String

        @JavascriptInterface
        fun openNotiSettings()

        @JavascriptInterface
        fun getBanks()

        @JavascriptInterface
        fun openDeepLink(params: String?)

        @JavascriptInterface
        fun showToast(params: String?)

        @JavascriptInterface
        fun openSelectBox(params: String?)

        @JavascriptInterface
        fun showAd(params: String?)

        @JavascriptInterface
        fun hideAd()

        @JavascriptInterface
        fun openNewView(params: String?)

        @JavascriptInterface
        fun finish()

        @JavascriptInterface
        fun getTransactions(params: String?)
    }
}