package com.tenqube.visualbase.infrastructure.adapter.parser.rule

import com.tenqube.visualbase.domain.resource.ParsingRuleDto
import com.tenqube.visualbase.domain.util.PrefStorage
import com.tenqube.visualbase.service.resource.ResourceAppService

class ParsingRuleService(
    private val resourceAppService: ResourceAppService,
    private val prefStorage: PrefStorage) {

    suspend fun getParsingRule(): ParsingRuleDto {
        val clientVersion = prefStorage.getRuleVersion()
        val serverVersion = resourceAppService.getVersion()

        return resourceAppService.getParsingRule(
            clientVersion,
            serverVersion
        )
    }

    suspend fun getParsingRuleWhenNoSender(): ParsingRuleDto? {
        val cnt = prefStorage.getSyncCnt()
        val newSyncCnt = cnt + 1
        return if(newSyncCnt == 10) {
            getParsingRule().also {
                prefStorage.saveSyncCnt(0)
            }
        } else {
            prefStorage.saveSyncCnt(newSyncCnt)
            null
        }
    }
}