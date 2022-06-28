package com.tenqube.visual_ibk_sdk_android.bridge.dto.request


data class GetTransactionsRequest(val data: GetTransactionsDto) : Request {
    override fun checkParams() {
    }
}

data class GetTransactionsDto(
    val year: Int,
    val month: Int,
    val periodByMonth: Int
)

