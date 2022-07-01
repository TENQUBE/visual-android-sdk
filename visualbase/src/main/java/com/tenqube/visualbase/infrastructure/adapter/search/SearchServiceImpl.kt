package com.tenqube.visualbase.infrastructure.adapter.search

import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import com.tenqube.visualbase.domain.search.SearchService
import com.tenqube.visualbase.domain.util.getValue
import com.tenqube.visualbase.infrastructure.adapter.search.remote.SearchRemoteDataSource

class SearchServiceImpl(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchService {
    override suspend fun search(request: SearchRequest): SearchResult {
        return searchRemoteDataSource.search(request).getValue()
    }
}