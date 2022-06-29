package com.tenqube.webui.dto

data class ShowSelectBox(
    val request: SelectBoxRequest,
    val callback: (selectBox: SelectBoxItem) -> Unit
)

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
