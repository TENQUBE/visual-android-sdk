package com.tenqube.webui.component.bottomsheet.model

import com.tenqube.webui.component.utils.Validator
import com.tenqube.webui.dto.common.RequestBody

data class OpenSelectBoxRequest(
    val title: String,
    val selectColor: String,
    val list: List<OpenSelectBoxItem>
) : RequestBody {

    override fun checkParams() {
        Validator.notNull(title)
        Validator.notNull(selectColor)
        Validator.notNull(list)

        list.forEach {
            it.checkParams()
        }
    }
}

data class OpenSelectBoxItem(
    val name: String,
    val orderByType: Int,
    var isSelected: Boolean
) : RequestBody {
    override fun checkParams() {

        Validator.notNull(name)
        Validator.notNull(orderByType)
        Validator.notNull(isSelected)
    }
}
