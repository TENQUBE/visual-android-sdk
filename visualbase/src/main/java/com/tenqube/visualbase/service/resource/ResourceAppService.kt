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
    private var serverVersion = 0
    private var lastSyncTime: Long = 0
    suspend fun getVersion(): Int = withContext(ioDispatcher) {
        return@withContext if(serverVersion == 0 || checkSyncTime()) {
            resourceService.getVersion().parsingRule.also {
                serverVersion = it
            }
        } else {
            serverVersion
        }
    }

    private fun checkSyncTime(): Boolean {
        return (System.currentTimeMillis() - lastSyncTime > 12* 60 * 60 * 1000).also {
            if(it) {
                lastSyncTime = System.currentTimeMillis()
            }
        }
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
