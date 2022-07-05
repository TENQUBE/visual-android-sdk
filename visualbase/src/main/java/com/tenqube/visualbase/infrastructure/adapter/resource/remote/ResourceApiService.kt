package com.tenqube.visualbase.infrastructure.adapter.resource.remote

import retrofit2.http.*

interface ResourceApiService {
    //    @GET("resource/version")
    @GET
    suspend fun syncVersion(@Url url: String, @HeaderMap header: Map<String, String>): VersionResponse

    @GET
    suspend fun syncParsingRule(
        @Url url: String, @HeaderMap header: Map<String, String>,
        @Query("type") type: String,
        @Query("clientVersion") clientVersion: Int,
        @Query("serverVersion") serverVersion: Int): SyncParsingRuleResponse

    //    @GET("resource/notification-apps")
    @GET
    suspend fun syncNotificationApps(@Url url: String, @HeaderMap header: Map<String, String>): NotificationPkgResponse

    //    @GET("resource/parsing-rule/key")
    @GET
    suspend fun syncParsingRuleKey(@Url url: String, @HeaderMap header: Map<String, String>): ParsingRuleKey

    //    @GET("resource")
    @GET
    suspend fun syncAnalysis(
        @Url url: String, @HeaderMap header: Map<String, String>,
        @Query("type") type: String,
        @Query("clientVersion") clientVersion: Int,
        @Query("serverVersion") serverVersion: Int): AnalysisResponse

    //    @GET("resource/notification")
    @GET
    suspend fun syncNotification(@Url url: String, @HeaderMap header: Map<String, String>): NotificationResponse

    companion object {
        const val CATEGORY = "category"
        const val ANALYSIS = "analysis"
        const val PARSING_RULE = "parsingRule"
    }
}