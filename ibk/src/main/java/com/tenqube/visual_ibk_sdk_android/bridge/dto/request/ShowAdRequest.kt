package com.tenqube.visual_ibk_sdk_android.bridge.dto.request


data class ShowAdRequest(val data: ShowAdDto) : Request {
    override fun checkParams() {
    }
}

data class ShowAdDto(
    val unitId: String,
    val container: AdContainer,
    val button: AdButton
)

data class AdContainer(
    val bgColor: String
)

data class AdButton(
    val bgColor: String,
    val textColor: String
)