package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class StatusBarRequest(val request: String) : RequestBody {

    override fun checkParams() {
    }
}
