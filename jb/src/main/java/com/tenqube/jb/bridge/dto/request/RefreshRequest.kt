package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class RefreshRequest(val data: RefreshRequestDto) : RequestBody {
    override fun checkParams() {
    }
}

data class RefreshRequestDto(val enabled: Boolean)
