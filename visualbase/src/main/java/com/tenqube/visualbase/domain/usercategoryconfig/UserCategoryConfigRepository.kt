package com.tenqube.visualbase.domain.usercategoryconfig

interface UserCategoryConfigRepository {
    suspend fun findAll(): List<UserCategoryConfig>
    suspend fun findById(id: String): UserCategoryConfig?
    suspend fun save(items: List<UserCategoryConfig>)
}
