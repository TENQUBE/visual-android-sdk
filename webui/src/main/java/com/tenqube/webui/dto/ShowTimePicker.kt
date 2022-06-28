package com.tenqube.webui.dto

data class ShowTimePicker(
    val request: TimePickerRequest,
    val callback:(time: String) -> Unit
)

data class TimePickerRequest(
    val date: String
)
