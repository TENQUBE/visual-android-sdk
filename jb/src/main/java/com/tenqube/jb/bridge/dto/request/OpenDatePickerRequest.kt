package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.DatePickerRequest

data class OpenDatePickerRequest(val date: String): RequestBody {
    fun asDomain(): DatePickerRequest {
        return DatePickerRequest(date)
    }

    override fun checkParams() {
    }
}
