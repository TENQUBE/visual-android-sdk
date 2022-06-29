package com.tenqube.ibk.bridge

import android.webkit.JavascriptInterface

interface Bridge {

    interface System {
        val bridgeName: String

        @JavascriptInterface
        fun openNotiSettings()

        @JavascriptInterface
        fun openDeepLink(params: String?)
    }

    interface Repository {
        val bridgeName: String

        @JavascriptInterface
        fun getBanks()

        @JavascriptInterface
        fun getTransactions(params: String?)
    }

    interface UI {
        val bridgeName: String

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
    }
}
