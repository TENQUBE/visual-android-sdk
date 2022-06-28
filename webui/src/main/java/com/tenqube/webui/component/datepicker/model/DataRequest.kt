package com.tenqube.webui.component.datepicker.model

import com.tenqube.webui.component.utils.Validator
import com.tenqube.webui.component.utils.Validator.notNull
import com.tenqube.webui.dto.common.Request
import com.tenqube.webui.exception.ParameterException
import java.io.Serializable

class DateRequest(
    val date: String? = null,
    val callbackJS: String? = null
) : Request(), Serializable {

    @Throws(ParameterException::class)
    override fun checkParams() {
        date?.let { Validator.isYMD(date) }
        notNull(callbackJS)
    }
}
