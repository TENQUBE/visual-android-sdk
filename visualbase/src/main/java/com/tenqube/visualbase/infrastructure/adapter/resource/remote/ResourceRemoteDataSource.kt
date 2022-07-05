package com.tenqube.visualbase.infrastructure.adapter.resource.remote

import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.domain.util.PrefStorage
import com.tenqube.visualbase.domain.util.Result
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ResourceRemoteDataSource(
    private val resourceApiService: ResourceApiService,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val baseUrl = ""

    private fun getUrl(): String {
        return prefStorage.getResourceUrl()
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = prefStorage.getAccessToken()
        map["x-api-key"] = prefStorage.getResourceApiKey()

        return map
    }

    suspend fun getVersion(): VersionDto {
        return when (val response = safeApiCall(ioDispatcher) {
            resourceApiService.ㅎ(
                getUrl(),
                getHeader(),
                request
            )
        }) {
            is ResultWrapper.Success -> {
                Result.Success(response.value)
            }
            is ResultWrapper.NetworkError -> {
                Result.Error(Exception("network error"))
            }
            is ResultWrapper.GenericError -> {
                Result.Error(Exception(response.error?.toString() ?: "generic error"))
            }
        }
    }
}