package com.tenqube.webui

import com.tenqube.webui.dto.*

interface UIService {
    fun finish()

    fun onClickSound()

    fun showToast(msg: String)

    fun showDialog(request: ShowDialog)

    fun showSelectBox(request: ShowSelectBox)

    fun openNewView(request: OpenNewViewDto)

    fun showDatePicker(request: ShowDatePicker)

    fun showTimePicker(request: ShowTimePicker)
}
