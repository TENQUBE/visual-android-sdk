package com.tenqube.visualbase.infrastructure.adapter.currency.remote

import com.tenqube.visualbase.domain.currency.CurrencyRequest
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.util.Result
import com.tenqube.visualbase.infrastructure.util.ErrorMsg
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CurrencyRemoteDataSource(
    private val currencyApiService: CurrencyApiService,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val baseUrl = ""

    private fun getUrl(path: String): String {
        return "$baseUrl/${prefStorage.layer}/$path"
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = prefStorage.accessToken
        map["x-api-key"] = prefStorage.apiKey

        return map
    }

    suspend fun exchange(request: CurrencyRequest): CurrencyResponse {
        return when (
            val response = safeApiCall(ioDispatcher) {
                currencyApiService.exchange(
                    getUrl("currency/rate/${request.from}/${request.to}"),
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
}
