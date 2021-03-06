package com.tenqube.visualbase.infrastructure.adapter.search.remote

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import com.tenqube.visualbase.infrastructure.util.ErrorMsg
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SearchRemoteDataSource(
    private val searchApiService: SearchApiService,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private fun getUrl(): String {
        return prefStorage.searchUrl
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = prefStorage.accessToken
        map["x-api-key"] = prefStorage.searchApiKey

        return map
    }

    suspend fun search(request: SearchRequest): SearchResult {
        return when (
            val response = safeApiCall(ioDispatcher) {
                searchApiService.searchCompany(
                    getUrl(),
                    getHeader(),
                    request
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
