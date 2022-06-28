package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenNewViewRequest(val data: OpenNewViewDto) : RequestBody {
    override fun checkParams() {
    }
}

data class OpenNewViewDto(
    val type: String,
    val url: String
)
