package com.tenqube.visualbase.domain.resource

import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.visualbase.domain.util.Result

interface ResourceRepository {
    fun getParsingRule(
        clientVersion: Int,
        serverVersion: Int
    ) : Result<ParsingRuleDto>
}