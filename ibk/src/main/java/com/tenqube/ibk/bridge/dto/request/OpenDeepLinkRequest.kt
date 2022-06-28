package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenDeepLinkRequest(val data: OpenDeepLinkDto) : RequestBody {
    override fun checkParams() {
    }
}

data class OpenDeepLinkDto(
    val url: String
)
