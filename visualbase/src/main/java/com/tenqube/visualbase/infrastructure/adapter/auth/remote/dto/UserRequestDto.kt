package com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto

import java.io.Serializable

data class UserRequestDto(
    val uid: String,
    val adId: String
) : Serializable
