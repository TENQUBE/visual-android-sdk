package com.tenqube.ibkreceipt.bridge.dto.request

import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.webui.dto.ColorOption

data class ShowAdRequest(
    val unitId: String,
    val container: AdContainer,
    val button: AdButton
) : RequestBody {
    override fun checkParams() {
    }
}

data class AdContainer(
    val bgColor: String
) {
    fun asDomain(): ColorOption {
        return ColorOption(bgColor, "")
    }
}

data class AdButton(
    val bgColor: String,
    val textColor: String
) {
    fun asDomain(): ColorOption {
        return ColorOption(bgColor, textColor)
    }
}
