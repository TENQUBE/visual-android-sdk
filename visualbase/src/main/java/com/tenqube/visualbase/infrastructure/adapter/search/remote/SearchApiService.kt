package com.tenqube.visualbase.infrastructure.adapter.search.remote

import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import retrofit2.http.*

interface SearchApiService {
    @POST
    suspend fun searchCompany(
        @Url url: String,
        @HeaderMap header: Map<String, String>,
        @Body req: SearchRequest): SearchResult
}