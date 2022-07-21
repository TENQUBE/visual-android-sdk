package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenNewViewRequest(
    val type: String,
    val url: String
) : RequestBody {
    fun asDomain(): com.tenqube.webui.dto.OpenNewViewDto {
        return com.tenqube.webui.dto.OpenNewViewDto(type, url)
    }

    override fun checkParams() {
    }
}
