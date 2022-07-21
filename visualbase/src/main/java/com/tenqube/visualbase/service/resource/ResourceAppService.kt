package com.tenqube.visualbase.service.resource

import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto

class ResourceAppService(
    private val resourceService: ResourceService
) {
    suspend fun getVersion(): Int {
        return resourceService.getVersion().parsingRule
    }

    suspend fun getParsingRule(clientVersion: Int, serverVersion: Int):
        ParsingRuleDto {
        return resourceService
            .getParsingRule(
                clientVersion,
                serverVersion
            )
    }
}
