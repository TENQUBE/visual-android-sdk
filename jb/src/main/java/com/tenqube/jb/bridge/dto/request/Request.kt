package com.tenqube.jb.bridge.dto.request

import com.tenqube.jb.bridge.error.ParameterError

interface Request {
    @Throws(ParameterError::class)
    fun checkParams()
}
