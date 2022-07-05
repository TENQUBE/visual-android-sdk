package com.tenqube.visualbase.infrastructure.adapter.resource

import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import com.tenqube.visualbase.domain.search.SearchService
import com.tenqube.visualbase.domain.util.Result
import com.tenqube.visualbase.domain.util.getValue
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.ResourceRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.search.remote.SearchRemoteDataSource

class ResourceServiceImpl(
    private val resourceRemoteDataSource: ResourceRemoteDataSource
) : ResourceService {
    override fun getVersion(): VersionDto {
        resourceRemoteDataSource.getVersion()
    }

    override fun getParsingRule(clientVersion: Int, serverVersion: Int): Result<ParsingRuleDto> {
        TODO("Not yet implemented")
    }

}