package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class ShowToastRequest(val message: String) : RequestBody {
    override fun checkParams() {
    }
}
