package com.tenqube.jb.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.ShowDialogRequest

data class OpenConfirmRequest(
    val title: String,
    val message: String,
    val positive: ButtonDto,
    val negative: ButtonDto
) : RequestBody {
    fun asDomain(): ShowDialogRequest {
        return ShowDialogRequest(
            title,
            message,
            positive.asDomain(),
            negative.asDomain()
        )
    }

    override fun checkParams() {
    }
}

data class ButtonDto(
    val button: ButtonDetailDto
) {
    fun asDomain(): com.tenqube.webui.dto.ButtonDto {
        return com.tenqube.webui.dto.ButtonDto(
            button = com.tenqube.webui.dto.ButtonDetailDto(
                button.text,
                button.color,
                button.bgColor
            )
        )
    }
}

data class ButtonDetailDto(
    val text: String,
    val color: String,
    val bgColor: String
)
