package com.tenqube.visualbase.domain.usercategoryconfig

interface UserCategoryConfigRepository {
    suspend fun findAll(): Result<List<UserCategoryConfig>>
    suspend fun save(items: List<UserCategoryConfig>): Result<Unit>
}
