package com.tenqube.jb.bridge.dto.request

data class ShowTimePickerRequest(val data: ShowTimePickerDto) : Request {
    override fun checkParams() {
    }
}

data class ShowTimePickerDto(
    val date: String
)