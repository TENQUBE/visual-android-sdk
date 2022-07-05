package com.tenqube.visualbase.domain.util

interface PrefStorage {
    fun getAccessToken(): String
    fun saveAccessToken(accessToken: String)

    fun getLayer(): String
    fun saveLayer(layer: String)

    fun getApiKey(): String
    fun saveApiKey(apiKey: String)

    fun getSearchUrl(): String
    fun getSearchApiKey(): String

    fun getResourceUrl(): String
    fun getResourceApiKey(): String

    fun getRuleVersion(): Int
    fun saveRuleVersion(version: Int)

    fun getSyncCnt(): Int
    fun saveSyncCnt(syncCnt: Int)
}