package com.tenqube.visualbase.domain.auth

import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto

interface AuthService {
    fun signUp(request: UserRequestDto): UserResultDto
    fun reissue(refreshToken: String): Token
    fun signOut()
}