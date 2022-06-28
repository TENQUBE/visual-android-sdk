package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class ShowToastRequest(val data: ShowToastDto) : RequestBody {
    override fun checkParams() {
    }
}

data class ShowToastDto(val message: String)
