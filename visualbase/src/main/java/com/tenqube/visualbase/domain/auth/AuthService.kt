package com.tenqube.visualbase.domain.auth

import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto

interface AuthService {
    suspend fun signUp(request: UserRequestDto): UserResultDto
    suspend fun reissue(refreshToken: String): Token
    suspend fun signOut()
}