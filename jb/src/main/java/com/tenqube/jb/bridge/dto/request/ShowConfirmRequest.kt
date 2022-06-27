package com.tenqube.jb.bridge.dto.request


data class ShowConfirmRequest(val data: ShowConfirmDto) : Request {
    override fun checkParams() {
    }
}

data class ShowConfirmDto(
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

