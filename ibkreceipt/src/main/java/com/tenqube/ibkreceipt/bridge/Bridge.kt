package com.tenqube.ibkreceipt.bridge

import android.webkit.JavascriptInterface

interface Bridge {

    interface UI {
        val bridgeName: String

        @JavascriptInterface
        fun showToast(params: String?)

        @JavascriptInterface
        fun setRefreshEnabled(params: String?)

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
        fun openNotiSettings()

        @JavascriptInterface
        fun openDeepLink(params: String?)

        @JavascriptInterface
        fun getBanks()

        @JavascriptInterface
        fun getTransactions(params: String?)

        @JavascriptInterface
        fun getNotiBanks()

        @JavascriptInterface
        fun setNotiEnabled(params: String?)
    }
}
