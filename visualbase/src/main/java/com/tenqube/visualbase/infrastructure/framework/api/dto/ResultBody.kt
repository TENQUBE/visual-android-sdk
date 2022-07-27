package com.tenqube.visualbase.infrastructure.framework.api.dto

import com.google.gson.annotations.SerializedName

data class ResultBody<T>(@SerializedName("results") val results: T?)
