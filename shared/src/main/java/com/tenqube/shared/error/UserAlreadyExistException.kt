package com.tenqube.shared.error

import java.lang.Exception

class UserAlreadyExistException(message: String?) : Exception(message)
