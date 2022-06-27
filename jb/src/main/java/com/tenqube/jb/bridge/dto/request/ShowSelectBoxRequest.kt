package com.tenqube.jb.bridge.dto.request


data class ShowSelectBoxRequest(val data: ShowSelectBoxDto) : Request {
    override fun checkParams() {
    }
}

data class ShowSelectBoxDto(
    val title: String,
    val selectedColor: String,
    val data: List<SelectBoxItemDto>
)

data class SelectBoxItemDto(
    val name: String,
    val orderByType: Int,
    val isSelected: Boolean
)
