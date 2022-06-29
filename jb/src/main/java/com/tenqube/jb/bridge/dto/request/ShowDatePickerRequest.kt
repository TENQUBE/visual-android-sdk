package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.DatePickerRequest

data class ShowDatePickerRequest(val data: ShowDatePickerDto) :
    RequestBody {
    override fun checkParams() {
    }
}

data class ShowDatePickerDto(val date: String) {
    fun asDomain(): DatePickerRequest {
        return DatePickerRequest(date)
    }
}
