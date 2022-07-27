package com.tenqube.visualbase.infrastructure.data.transaction.remote

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.infrastructure.framework.api.dto.VisualApiConfig
import com.tenqube.visualbase.infrastructure.util.ErrorMsg
import com.tenqube.visualbase.infrastructure.util.ResultWrapper
import com.tenqube.visualbase.infrastructure.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TransactionRemoteDataSource(
    private val api: TransactionApiService,
    private val prefStorage: PrefStorage,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private fun getUrl(path: String): String {
        return "${VisualApiConfig.URL}/${prefStorage.layer}/$path"
    }

    private fun getHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["Authorization"] = prefStorage.accessToken
        map["x-api-key"] = prefStorage.apiKey

        return map
    }

    suspend fun saveTransaction(request: TransactionRequest) {
        return when (
            val response = safeApiCall(ioDispatcher) {
                api.saveTransactions(
                    getUrl("transaction"),
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
