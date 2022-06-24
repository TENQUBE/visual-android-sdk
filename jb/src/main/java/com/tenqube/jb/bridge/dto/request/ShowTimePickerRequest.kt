package com.tenqube.jb.bridge.dto.request

import com.tenqube.reward.domain.ui.dto.ShowTimePickerDto

data class ShowTimePickerRequest(val data: ShowTimePickerDto) : Request {
    override fun checkParams() {
    }
}