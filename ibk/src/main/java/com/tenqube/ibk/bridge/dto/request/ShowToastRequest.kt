package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class ShowToastRequest(val data: ShowToastDto) : RequestBody {
    override fun checkParams() {
    }
}

data class ShowToastDto(val message: String)
