package com.tenqube.visualbase.domain.resource

import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.domain.util.Result

interface ResourceService {

    fun getVersion(): VersionDto

    fun getParsingRule(
        clientVersion: Int,
        serverVersion: Int
    ) : Result<ParsingRuleDto>
}