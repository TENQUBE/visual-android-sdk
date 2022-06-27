package com.tenqube.jb.bridge.dto.request

data class ShowDatePickerRequest(val data: ShowDatePickerDto) : Request {
    override fun checkParams() {
    }
}

data class ShowDatePickerDto(val date: String) {
    fun asDomain(): com.tenqube.webui.dto.ShowDatePickerDto {
        return com.tenqube.webui.dto.ShowDatePickerDto(date)
    }
}
