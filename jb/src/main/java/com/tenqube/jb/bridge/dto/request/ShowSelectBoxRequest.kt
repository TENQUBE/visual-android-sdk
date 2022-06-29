package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody


data class ShowSelectBoxRequest(val data: ShowSelectBoxDto) :
    RequestBody {
    override fun checkParams() {
    }
}

data class ShowSelectBoxDto(
    val title: String,
    val selectedColor: String,
    val data: List<SelectBoxItemDto>
) {
    fun asDomain(): com.tenqube.webui.dto.SelectBoxRequest {
        return com.tenqube.webui.dto.SelectBoxRequest(
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
    fun asDomain(): com.tenqube.webui.dto.SelectBoxItem {
        return com.tenqube.webui.dto.SelectBoxItem(
            name,
            orderByType,
            isSelected
        )
    }
}
