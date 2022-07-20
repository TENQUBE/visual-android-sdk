package com.tenqube.visualbase.domain.user.command

data class CreateUser(
    val uid: String,
    val gender: Int,
    val birth: Int
)
