package com.tenqube.visualbase.infrastructure.adapter.resource.remote.dto

import com.google.gson.annotations.SerializedName
import com.tenqube.visualbase.domain.resource.dto.ParsingRuleDto
import tenqube.parser.model.Bank
import tenqube.parser.model.ParsingRule
import tenqube.parser.model.RegData
import tenqube.parser.model.Sender

data class SyncParsingRuleDto(
    @SerializedName("signature") val signature: String,
    @SerializedName("resource") val resource: ParsingRuleResponse
)

data class ParsingRuleResponse(
    val signature: String,
    val securityKey: String,
    val tranCount: Int,
    val ruleVersion: Int,
    val regDatas: List<RegData>,
    val senders: List<Sender>,
    val banks: List<Bank>
) {
    fun asDomain(): ParsingRuleDto {
        return ParsingRuleDto(
            ParsingRule(
                securityKey,
                tranCount,
                ruleVersion,
                ArrayList(regDatas),
                ArrayList(senders),
                ArrayList(banks),
                arrayListOf()
            )
        )
    }
}