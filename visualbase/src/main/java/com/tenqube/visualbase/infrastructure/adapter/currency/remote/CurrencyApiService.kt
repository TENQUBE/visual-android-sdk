package com.tenqube.visualbase.infrastructure.adapter.currency.remote

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface CurrencyApiService {
    @GET
    suspend fun exchange(
        @Url url: String,
        @HeaderMap header: Map<String, String>
    ): CurrencyResponse
}

data class CurrencyResponse(val rate: Float)