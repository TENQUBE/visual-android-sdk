package com.tenqube.webui.dto

import android.view.View

data class ShowAd(
    val unitId: String,
    val container: ColorOption?,
    val button: ColorOption?,
    val callback: (View) -> Unit
)

data class ColorOption(
    val bgColor: String,
    val textColor: String?
)
