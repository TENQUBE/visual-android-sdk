package com.tenqube.jb.bridge.dto.request

data class ShowToastRequest(val data: ShowToastDto) : Request {
    override fun checkParams() {
    }
}

data class ShowToastDto(val message: String)
