package com.tenqube.visual_ibk_sdk_android.bridge.dto.response

data class BanksDto(
    val banks: List<BankDto>
)

data class BankDto(
    val id: String,
    val name: String,
    val symbol: String
)
