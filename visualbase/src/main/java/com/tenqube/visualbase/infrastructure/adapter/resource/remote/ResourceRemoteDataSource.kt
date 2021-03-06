package com.tenqube.visualbase.infrastructure.adapter.resource.remote

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.dto.SyncParsingRuleDto
import com.tenqube.visualbase.infrastructure.util.ErrorMsg
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ResourceRemoteDataSource(
    private val resourceApiService: ResourceApiService,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private fun getUrl(): String {
        return prefStorage.resourceUrl
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["service"] = prefStorage.service
        map["x-api-key"] = prefStorage.resourceApiKey

        return map
    }

    suspend fun getVersion(): VersionDto {
        return when (
            val response = safeApiCall(ioDispatcher) {
                resourceApiService.syncVersion(
                    "${getUrl()}/resource/version",
                    getHeader()
                )
            }
        ) {
            is ResultWrapper.Success -> {
                response.value
            }
            is ResultWrapper.NetworkError -> {
                throw Exception(ErrorMsg.NETWORK.msg)
            }
            is ResultWrapper.GenericError -> {
                throw Exception(response.error?.toString() ?: ErrorMsg.GENERIC.msg)
            }
        }
    }

    suspend fun getParsingRule(
        clientVersion: Int,
        serverVersion: Int
    ): SyncParsingRuleDto {
        return when (
            val response = safeApiCall(ioDispatcher) {
                resourceApiService.syncParsingRule(
                    "${getUrl()}/resource/v2",
                    getHeader(),
                    ResourceApiService.PARSING_RULE,
                    clientVersion,
                    serverVersion
                )
            }
        ) {
            is ResultWrapper.Success -> {
                response.value
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
