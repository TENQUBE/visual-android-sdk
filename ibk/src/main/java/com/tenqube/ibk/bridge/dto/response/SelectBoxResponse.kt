package com.tenqube.ibk.bridge.dto.response

data class SelectBoxResponse(
    val result: SelectBox
)

data class SelectBox(
    val name: String,
    val orderByType: Int,
    val isSelected: Boolean
)
