package com.tenqube.visual_ibk_sdk_android.bridge.dto.request

data class ShowToastRequest(val data: ShowToastDto) : Request {
    override fun checkParams() {
    }
}

data class ShowToastDto(val message: String)
