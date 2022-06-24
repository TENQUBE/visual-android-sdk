package com.tenqube.jb.bridge.dto.request

import com.tenqube.jb.bridge.dto.request.Request
import com.tenqube.reward.domain.ui.dto.ShowConfirmDto

data class ShowConfirmRequest(val data: ShowConfirmDto) : Request {
    override fun checkParams() {
    }
}


