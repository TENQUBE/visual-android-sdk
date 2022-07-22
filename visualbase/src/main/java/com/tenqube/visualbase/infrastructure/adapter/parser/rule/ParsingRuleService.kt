package com.tenqube.visualbase.infrastructure.adapter.parser.rule

import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.service.resource.ResourceAppService

class ParsingRuleService(
    private val resourceAppService: ResourceAppService,
    private val prefStorage: PrefStorage
) {

    suspend fun getParsingRule(): ParsingRuleDto? {
        val clientVersion = prefStorage.ruleVersion
        val serverVersion = resourceAppService.getVersion()
        if(clientVersion >= serverVersion) {
            return  null
        }

        return resourceAppService.getParsingRule(
            clientVersion,
            serverVersion
        ).also {
            prefStorage.ruleVersion = serverVersion
        }
    }

    suspend fun getParsingRuleWhenNoSender(): ParsingRuleDto? {
        val cnt = prefStorage.syncCnt
        val newSyncCnt = cnt + 1
        return if (newSyncCnt == 10) {
            getParsingRule().also {
                prefStorage.syncCnt = 0
            }
        } else {
            prefStorage.syncCnt = newSyncCnt
            null
        }
    }
}
