package com.tenqube.visualbase.domain.user.command

import java.io.Serializable

data class CreateUser(
    val uid: String,
    val gender: Int,
    val birth: Int
) : Serializable
