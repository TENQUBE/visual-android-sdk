package com.tenqube.visualbase.infrastructure.adapter.auth

import com.tenqube.visualbase.domain.auth.AuthService
import com.tenqube.visualbase.domain.util.getValue
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto

class AuthServiceImpl(private val remote: AuthRemoteDataSource) : AuthService {
    override suspend fun signUp(request: UserRequestDto): UserResultDto {
        return remote.signUp(request)
    }

    override suspend fun reissue(refreshToken: String): Token {
        return remote.reissueToken(refreshToken)
    }

    override suspend fun signOut() {
    }
}
