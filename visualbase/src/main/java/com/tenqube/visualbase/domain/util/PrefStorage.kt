package com.tenqube.visualbase.domain.util

interface PrefStorage {
    fun getAccessToken(): String

    fun saveAccessToken(accessToken: String)

    fun getLayer(): String

    fun saveLayer(layer: String)

    fun getApiKey(): String
}