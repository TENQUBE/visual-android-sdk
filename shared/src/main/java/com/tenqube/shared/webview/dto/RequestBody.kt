package com.tenqube.shared.webview.dto

import com.tenqube.shared.error.ParameterError

interface RequestBody {
    @Throws(ParameterError::class)
    fun checkParams()
}
