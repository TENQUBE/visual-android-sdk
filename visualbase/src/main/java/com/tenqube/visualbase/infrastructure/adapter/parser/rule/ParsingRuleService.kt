package com.tenqube.visualbase.infrastructure.adapter.parser.rule

import com.tenqube.visualbase.domain.util.PrefStorage
import com.tenqube.visualbase.service.resource.ResourceAppService

class ParsingRuleService(
    private val resourceAppService: ResourceAppService,
    private val prefStorage: PrefStorage) {
    suspend fun sync() {

    }

    suspend fun syncWhenNoSender() {

    }
}