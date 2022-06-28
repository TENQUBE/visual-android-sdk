package com.tenqube.shared.webview.dto

enum class StatusCode(val code: Int) {
    Success(200),
    EmptyData(100),
    Forbidden(403),
    ParameterError(400),
    JSONParsingError(400),
    ServerError(500);

    companion object {
        fun fromCode(code: Int): StatusCode {
            return values().find { it.code == code } ?: ServerError
        }
    }
}
