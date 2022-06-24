package com.tenqube.webui.dto.common

import com.tenqube.webui.exception.ParameterException

interface RequestBody {
    @Throws(ParameterException::class)
    fun checkParams()
}
