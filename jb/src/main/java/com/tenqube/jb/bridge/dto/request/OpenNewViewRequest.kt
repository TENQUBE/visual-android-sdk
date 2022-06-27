package com.tenqube.jb.bridge.dto.request

data class OpenNewViewRequest(val data: OpenNewViewDto) : Request {
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