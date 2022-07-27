package com.tenqube.visualbase.infrastructure.adapter.auth.remote

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.Token
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto
import com.tenqube.visualbase.infrastructure.framework.api.dto.VisualApiConfig
import com.tenqube.visualbase.infrastructure.util.ErrorMsg
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AuthRemoteDataSource(
    private val api: AuthApi,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private fun getUrl(path: String): String {
        return "${VisualApiConfig.URL}/${prefStorage.layer}/$path"
    }

    private fun getHeader(authorization: String): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = authorization
        map["x-api-key"] = prefStorage.apiKey
        return map
    }

    suspend fun signUp(request: UserRequestDto): UserResultDto {
        return when (val response =
            safeApiCall(ioDispatcher) {
                api.signUp(getUrl("users/sign-up"), getHeader(""), request)
            }) {
            is ResultWrapper.Success -> {
                response.value.results!!
            }
            is ResultWrapper.NetworkError -> {
                throw Exception(ErrorMsg.NETWORK.msg)
            }
            is ResultWrapper.GenericError -> {
                throw Exception(response.error?.toString() ?: ErrorMsg.GENERIC.msg)
            }
        }
    }

    suspend fun reissueToken(refreshToken: String): Token {
        return when (val response =
            safeApiCall(ioDispatcher) {
                api.reissueToken(getUrl("users/reissue"), getHeader(refreshToken))
            }) {
            is ResultWrapper.Success -> {
                response.value.results!!
            }
            is ResultWrapper.NetworkError -> {
                throw Exception(ErrorMsg.NETWORK.msg)
            }
            is ResultWrapper.GenericError -> {
                throw Exception(response.error?.toString() ?: ErrorMsg.GENERIC.msg)
            }
        }
    }
}