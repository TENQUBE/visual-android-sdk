package com.tenqube.visualbase.service.resource

import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResourceAppService(
    private val resourceService: ResourceService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVersion(): Int = withContext(ioDispatcher) {
        return@withContext resourceService.getVersion().parsingRule
    }

    suspend fun getParsingRule(clientVersion: Int, serverVersion: Int):
        ParsingRuleDto = withContext(ioDispatcher) {
        return@withContext resourceService
            .getParsingRule(
                clientVersion,
                serverVersion
            )
    }
}
