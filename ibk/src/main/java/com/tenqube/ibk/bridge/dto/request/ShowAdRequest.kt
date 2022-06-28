package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody


data class ShowAdRequest(val data: ShowAdDto) : RequestBody {
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