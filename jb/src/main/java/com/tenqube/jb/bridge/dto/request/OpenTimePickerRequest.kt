package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.TimePickerRequest

data class OpenTimePickerRequest(
    val date: String
) : RequestBody {
    fun asDomain(): TimePickerRequest {
        return TimePickerRequest(date)
    }

    override fun checkParams() {
    }
}
