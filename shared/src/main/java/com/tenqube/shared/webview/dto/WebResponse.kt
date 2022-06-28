package com.tenqube.shared.webview.dto

import java.io.Serializable

data class WebResponse<T>(
    val statusCode: Int,
    val msg: String,
    val body: T? = null
) : Serializable
