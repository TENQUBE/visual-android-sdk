package com.tenqube.visualbase.infrastructure.util

import com.google.gson.annotations.SerializedName
import com.tenqube.shared.util.Utils.fromJson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.io.Serializable

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) : ResultWrapper<Nothing>()
    object NetworkError : ResultWrapper<Nothing>()
}

data class ErrorResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("message") val message: String
) : Serializable

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        val result = throwable.response()?.errorBody()?.string()
        result?.let {
            fromJson(
                result,
                ErrorResponse::class.java
            )
        }
    } catch (exception: Exception) {
        null
    }
}
