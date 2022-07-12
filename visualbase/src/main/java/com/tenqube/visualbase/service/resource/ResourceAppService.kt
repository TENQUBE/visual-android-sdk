package com.tenqube.visualbase.service.resource

import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto

class ResourceAppService(
    private val resourceRepository: ResourceService
) {
    suspend fun getVersion(): Int {
        return resourceRepository.getVersion().parsingRule
    }

    suspend fun getParsingRule(clientVersion: Int, serverVersion: Int):
        ParsingRuleDto {
        return resourceRepository
            .getParsingRule(
                clientVersion,
                serverVersion
            )
    }
}
