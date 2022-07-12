package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenDeepLinkRequest(
    val url: String
) : RequestBody {
    fun asDomain(): com.tenqube.webui.dto.OpenNewViewDto {
        return com.tenqube.webui.dto.OpenNewViewDto("external", url)
    }

    override fun checkParams() {
    }
}
