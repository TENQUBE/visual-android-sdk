package com.tenqube.webui.dto

data class ShowDialogDto(
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