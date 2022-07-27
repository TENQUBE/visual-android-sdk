package com.tenqube.visualbase.infrastructure.adapter.resource

import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.domain.util.getValue
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.ResourceRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.dto.SyncParsingRuleDto

class ResourceServiceImpl(
    private val resourceRemoteDataSource: ResourceRemoteDataSource
) : ResourceService {

    override suspend fun getVersion(): VersionDto {
        return resourceRemoteDataSource.getVersion()
    }

    override suspend fun getParsingRule(
        clientVersion: Int,
        serverVersion: Int
    ): ParsingRuleDto {
        return resourceRemoteDataSource.getParsingRule(
            clientVersion,
            serverVersion
        ).run {
            checkSignature(this)
            this.resource.asDomain()
        }
    }

    private fun checkSignature(rule: SyncParsingRuleDto) {
    }
}
