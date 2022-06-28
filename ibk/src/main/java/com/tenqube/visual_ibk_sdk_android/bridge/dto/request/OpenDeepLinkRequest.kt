package com.tenqube.visual_ibk_sdk_android.bridge.dto.request

data class OpenDeepLinkRequest(val data: OpenDeepLinkDto) : Request {
    override fun checkParams() {
    }
}

data class OpenDeepLinkDto(
    val url: String
)
