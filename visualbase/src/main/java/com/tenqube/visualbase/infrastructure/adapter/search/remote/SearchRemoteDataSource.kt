package com.tenqube.visualbase.infrastructure.adapter.search.remote

import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import com.tenqube.visualbase.domain.util.PrefStorage
import com.tenqube.visualbase.domain.util.Result
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

    private val baseUrl = ""

    private fun getUrl(): String {
        return prefStorage.getSearchUrl()
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = prefStorage.getAccessToken()
        map["x-api-key"] = prefStorage.getSearchApiKey()

        return map
    }

    suspend fun search(request: SearchRequest): Result<SearchResult> {
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
                Result.Success(response.value)
            }
            is ResultWrapper.NetworkError -> {
                Result.Error(Exception(ErrorMsg.NETWORK.msg))
            }
            is ResultWrapper.GenericError -> {
                Result.Error(Exception(response.error?.toString() ?: ErrorMsg.GENERIC.msg))
            }
        }
    }
}
