package com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto

import java.io.Serializable

data class UserResultDto(
    val secretKey: String,
    val search: ExternalApi,
    val resource: ExternalApi,
    val web: Web,
    val authorization: Authorization
): Serializable

data class ExternalApi(val url: String, val apiKey: String): Serializable

data class Web(val url: String, val iosUrl: String): Serializable

data class Authorization(val sdk: String) : Serializable

data class Token(val accessToken: String, val refreshToken: String) : Serializable
