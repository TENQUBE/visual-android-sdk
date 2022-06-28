package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody


data class GetTransactionsRequest(val data: GetTransactionsDto) : RequestBody {
    override fun checkParams() {
    }
}

data class GetTransactionsDto(
    val year: Int,
    val month: Int,
    val periodByMonth: Int
)

