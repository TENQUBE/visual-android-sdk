package com.tenqube.visualbase.infrastructure.adapter.resource.remote

import com.tenqube.visualbase.domain.resource.dto.VersionDto
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.dto.ParsingRuleKeyDto
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.dto.SyncParsingRuleDto
import retrofit2.http.*

interface ResourceApiService {
    //    @GET("resource/version")
    @GET
    suspend fun syncVersion(@Url url: String, @HeaderMap header: Map<String, String>): VersionDto

    @GET
    suspend fun syncParsingRule(
        @Url url: String, @HeaderMap header: Map<String, String>,
        @Query("type") type: String,
        @Query("clientVersion") clientVersion: Int,
        @Query("serverVersion") serverVersion: Int): SyncParsingRuleDto

    @GET
    suspend fun syncParsingRuleKey(@Url url: String, @HeaderMap header: Map<String, String>): ParsingRuleKeyDto

    companion object {
        const val PARSING_RULE = "parsingRule"
    }
}