package com.tenqube.visual_ibk_sdk_android.bridge.dto.request

data class OpenSelectBoxRequest(val data: OpenSelectBoxDto) : Request {
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
