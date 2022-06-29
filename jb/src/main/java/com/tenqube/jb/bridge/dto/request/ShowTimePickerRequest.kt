package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.TimePickerRequest

data class ShowTimePickerRequest(val data: ShowTimePickerDto) :
    RequestBody {
    override fun checkParams() {
    }
}

data class ShowTimePickerDto(
    val date: String
) {
    fun asDomain(): TimePickerRequest {
        return TimePickerRequest(date)
    }
}
