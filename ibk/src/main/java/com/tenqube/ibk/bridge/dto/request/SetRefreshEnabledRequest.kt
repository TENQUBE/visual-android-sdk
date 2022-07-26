package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class SetRefreshEnabledRequest(val request: Boolean) : RequestBody {
    override fun checkParams() {
    }
}
