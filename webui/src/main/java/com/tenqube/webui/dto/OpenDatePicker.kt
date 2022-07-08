package com.tenqube.webui.dto

data class OpenDatePicker(
    val request: DatePickerRequest,
    val callback: (date: String) -> Unit
)

data class DatePickerRequest(
    val date: String,
    val callbackJS: String? = null
)
