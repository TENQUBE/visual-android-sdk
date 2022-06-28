package com.tenqube.visual_ibk_sdk_android.bridge.dto.response

import java.io.Serializable

data class WebResponse<T>(
    val statusCode: Int,
    val msg: String,
    val body: T? = null
) : Serializable
