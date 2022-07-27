package com.tenqube.ibkreceipt.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class GetTransactionsRequest(
    val year: Int,
    val month: Int
) : RequestBody {
    override fun checkParams() {
    }
}
