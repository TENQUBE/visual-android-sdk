package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class ShowTimePickerRequest(val data: ShowTimePickerDto) :
    RequestBody {
    override fun checkParams() {
    }
}

data class ShowTimePickerDto(
    val date: String
) {
    fun asDomain(): com.tenqube.webui.dto.ShowTimePickerDto {
        return com.tenqube.webui.dto.ShowTimePickerDto(date)
    }
}