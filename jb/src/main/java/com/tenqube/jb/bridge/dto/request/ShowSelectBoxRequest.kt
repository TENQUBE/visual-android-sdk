package com.tenqube.jb.bridge.dto.request


data class ShowSelectBoxRequest(val data: ShowSelectBoxDto) : Request {
    override fun checkParams() {
    }
}

data class ShowSelectBoxDto(
    val title: String,
    val selectedColor: String,
    val data: List<SelectBoxItemDto>
) {
    fun asDomain(): com.tenqube.webui.dto.ShowSelectBoxDto {
        return com.tenqube.webui.dto.ShowSelectBoxDto(
            title,
            selectedColor,
            data.map { it.asDomain() }
        )
    }
}

data class SelectBoxItemDto(
    val name: String,
    val orderByType: Int,
    val isSelected: Boolean
) {
    fun asDomain(): com.tenqube.webui.dto.SelectBoxItemDto {
        return com.tenqube.webui.dto.SelectBoxItemDto(
            name,
            orderByType,
            isSelected
        )
    }
}
