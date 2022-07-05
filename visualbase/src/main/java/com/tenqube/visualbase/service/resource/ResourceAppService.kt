package com.tenqube.visualbase.service.resource

import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.util.getValue

class ResourceAppService(
    private val resourceRepository: ResourceService
) {
    suspend fun getVersion(): Int {
        return 0
    }

    suspend fun getParsingRule(clientVersion: Int, serverVersion: Int) :
            ParsingRuleDto {
        return resourceRepository
            .getParsingRule(
                clientVersion,
                serverVersion
            )
            .getValue()
    }

}