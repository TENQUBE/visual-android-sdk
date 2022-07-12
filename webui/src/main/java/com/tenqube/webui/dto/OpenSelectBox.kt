package com.tenqube.webui.dto

import java.io.Serializable

data class OpenSelectBox(
    val request: SelectBoxRequest,
    val callback: (selectBox: SelectBoxItem) -> Unit
) : Serializable

data class SelectBoxRequest(
    val title: String,
    val selectedColor: String,
    val data: List<SelectBoxItem>
)

data class SelectBoxItem(
    val name: String,
    val orderByType: Int,
    val isSelected: Boolean
)
