package com.tenqube.visualbase.service.resource

import com.tenqube.visualbase.domain.resource.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.ResourceRepository
import com.tenqube.visualbase.domain.util.getValue

class ResourceAppService(
    private val resourceRepository: ResourceRepository
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