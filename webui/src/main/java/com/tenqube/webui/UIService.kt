package com.tenqube.webui

import com.tenqube.webui.component.dialog.DialogCallback
import com.tenqube.webui.dto.*

interface UIService {
    fun finish()

    fun onClickSound()

    fun showToast(msg: String)

    fun showDialog(request: ShowDialogDto, callback: DialogCallback)

    fun showSelectBox(request: ShowSelectBoxDto)

    fun openNewView(request: OpenNewViewDto)

    fun showDatePicker(request: ShowDatePickerDto)

    fun showTimePicker(request: ShowTimePickerDto)
}
