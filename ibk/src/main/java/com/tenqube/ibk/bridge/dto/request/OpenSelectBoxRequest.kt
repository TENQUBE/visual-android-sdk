package com.tenqube.ibk.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenSelectBoxRequest(val data: OpenSelectBoxDto) : RequestBody {
    override fun checkParams() {
    }
}

data class OpenSelectBoxDto(
    val title: String,
    val selectedColor: String,
    val data: List<SelectBoxItem>,
)

data class SelectBoxItem(
    val name: String,
    val orderByType: Int,
    val isSelected: Boolean
)
