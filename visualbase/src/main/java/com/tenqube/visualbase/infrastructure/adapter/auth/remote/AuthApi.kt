package com.tenqube.visualbase.infrastructure.adapter.auth.remote

import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto
import com.tenqube.visualbase.infrastructure.framework.api.dto.ResultBody
import retrofit2.http.*

interface AuthApi {
    @POST
    suspend fun signUp(@Url url: String, @HeaderMap header: Map<String, String>,
                       @Body request: UserRequestDto
    ): ResultBody<UserResultDto>

    @POST
    suspend fun reissueToken(@Url url: String, @HeaderMap header: Map<String, String>): ResultBody<Token>
}