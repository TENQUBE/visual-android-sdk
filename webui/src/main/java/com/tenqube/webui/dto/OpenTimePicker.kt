package com.tenqube.webui.dto

data class OpenTimePicker(
    val request: TimePickerRequest,
    val callback: (time: String) -> Unit
)

data class TimePickerRequest(
    val date: String
)
