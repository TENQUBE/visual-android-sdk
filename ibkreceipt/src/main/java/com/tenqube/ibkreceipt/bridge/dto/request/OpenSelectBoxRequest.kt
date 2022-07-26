package com.tenqube.ibkreceipt.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody

data class OpenSelectBoxRequest(
    val title: String,
    val selectedColor: String,
    val body: List<SelectBoxItemDto>
) : RequestBody {
    fun asDomain(): com.tenqube.webui.dto.SelectBoxRequest {
        return com.tenqube.webui.dto.SelectBoxRequest(
            title,
            selectedColor,
            body.map { it.asDomain() }
        )
    }

    override fun checkParams() {
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

