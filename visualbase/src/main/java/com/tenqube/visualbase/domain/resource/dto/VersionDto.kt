package com.tenqube.visualbase.domain.resource.dto

data class VersionDto(
    val analysis: Int,
    val category: Int,
    val parsingRule: Int,
    val ad: Int,
    val notification: Int,
    val pkgVersion: Int
)