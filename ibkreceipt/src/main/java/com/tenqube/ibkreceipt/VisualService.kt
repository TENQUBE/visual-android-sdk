package com.tenqube.ibkreceipt

interface VisualService {
    fun start(command: UserArg)
}

data class UserArg(
    val uid: String,
    val birth: Int,
    val gender: VisualGender
)

enum class VisualGender {
    FEMALE,
    MALE
}