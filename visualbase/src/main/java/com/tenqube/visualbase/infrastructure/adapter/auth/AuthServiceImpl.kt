package com.tenqube.visualbase.infrastructure.adapter.auth

import com.tenqube.visualbase.domain.auth.AuthService
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto

class AuthServiceImpl(private val remote: AuthRemoteDataSource) : AuthService {
    override fun signUp(request: UserRequestDto): UserResultDto {
        TODO("Not yet implemented")
    }

    override fun reissue(refreshToken: String): Token {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}