package com.tenqube.visualbase.domain.resource

import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.dto.VersionDto

interface ResourceService {

    suspend fun getVersion(): VersionDto

    suspend fun getParsingRule(
        clientVersion: Int,
        serverVersion: Int
    ): ParsingRuleDto
}
