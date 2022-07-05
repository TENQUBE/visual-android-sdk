package com.tenqube.visualbase.infrastructure.adapter.resource.remote

import com.google.gson.annotations.SerializedName

data class SyncParsingRuleResponse(
        @SerializedName("signature") val signature: String,
        @SerializedName("resource") val resource: ParsingRuleResponse
) {
}