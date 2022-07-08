package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class RefreshRequest(val enabled: Boolean) : RequestBody {
    override fun checkParams() {
    }
}
