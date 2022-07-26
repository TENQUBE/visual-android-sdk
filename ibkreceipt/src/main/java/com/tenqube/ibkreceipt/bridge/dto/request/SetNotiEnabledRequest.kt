package com.tenqube.ibkreceipt.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class SetNotiEnabledRequest(val request: Boolean) : RequestBody {
    override fun checkParams() {
    }
}
