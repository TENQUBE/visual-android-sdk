package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenNewViewRequest(val data: OpenNewViewDto) : RequestBody {
    override fun checkParams() {
    }
}

data class OpenNewViewDto(
    val type: String,
    val url: String
) {
    fun asDomain(): com.tenqube.webui.dto.OpenNewViewDto {
        return com.tenqube.webui.dto.OpenNewViewDto(type, url)
    }
}
