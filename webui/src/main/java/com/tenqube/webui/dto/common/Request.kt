package com.tenqube.webui.dto.common

import com.tenqube.webui.exception.ParameterException

abstract class Request {
    @Throws(ParameterException::class)
    abstract fun checkParams()
}
