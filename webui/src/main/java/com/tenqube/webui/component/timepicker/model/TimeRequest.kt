package com.tenqube.webui.component.timepicker.model

import com.tenqube.webui.component.utils.Validator
import com.tenqube.webui.component.utils.Validator.notNull
import com.tenqube.webui.dto.common.Request
import com.tenqube.webui.exception.ParameterException
import java.io.Serializable

class TimeRequest(
    val time: String,
): Request(), Serializable {

    @Throws(ParameterException::class)
    override fun checkParams() {
        Validator.isTime(time)
    }
}