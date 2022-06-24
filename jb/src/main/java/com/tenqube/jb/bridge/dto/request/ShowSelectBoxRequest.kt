package com.tenqube.jb.bridge.dto.request

import com.tenqube.reward.domain.ui.dto.ShowSelectBoxDto

data class ShowSelectBoxRequest(val data: ShowSelectBoxDto) : Request {
    override fun checkParams() {
    }
}
