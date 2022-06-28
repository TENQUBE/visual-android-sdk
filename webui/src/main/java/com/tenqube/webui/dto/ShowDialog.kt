package com.tenqube.webui.dto

import com.tenqube.webui.component.dialog.DialogCallback

data class ShowDialog(
    val request: ShowDialogRequest,
    val callback: DialogCallback
)

data class ShowDialogRequest(
    val title: String,
    val message: String,
    val positive: ButtonDto,
    val negative: ButtonDto
)

data class ButtonDto(
    val button: ButtonDetailDto
)

data class ButtonDetailDto(
    val text: String,
    val color: String,
    val bgColor: String
)
