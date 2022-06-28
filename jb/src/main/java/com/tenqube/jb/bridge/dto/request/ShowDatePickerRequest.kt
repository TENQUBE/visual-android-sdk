package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class ShowDatePickerRequest(val data: ShowDatePickerDto) :
    RequestBody {
    override fun checkParams() {
    }
}

data class ShowDatePickerDto(val date: String) {
    fun asDomain(): com.tenqube.webui.dto.ShowDatePickerDto {
        return com.tenqube.webui.dto.ShowDatePickerDto(date)
    }
}
