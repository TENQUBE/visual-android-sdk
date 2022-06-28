package com.tenqube.visual_ibk_sdk_android.bridge.dto.request

import com.tenqube.visual_ibk_sdk_android.bridge.error.ParameterError

interface Request {
    @Throws(ParameterError::class)
    fun checkParams()
}
