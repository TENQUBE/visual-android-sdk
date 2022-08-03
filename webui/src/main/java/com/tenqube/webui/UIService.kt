package com.tenqube.webui

import com.tenqube.webui.dto.*

interface UIService {

    fun finish()

    fun onClickSound()

    fun showToast(msg: String)

    fun showDialog(request: ShowDialog)

    fun openSelectBox(request: OpenSelectBox)

    fun openNewView(request: OpenNewViewDto)

    fun openDatePicker(request: OpenDatePicker)

    fun openTimePicker(request: OpenTimePicker)

    fun openNotiSettings()

    fun setRefreshEnabled(enabled: Boolean)

    fun showAd(request: ShowAd)

    fun hideAd()

    fun openOverlayPermission(requestCode: Int)
}
