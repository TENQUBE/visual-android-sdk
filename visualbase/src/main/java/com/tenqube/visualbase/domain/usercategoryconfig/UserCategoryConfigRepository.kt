package com.tenqube.visualbase.domain.usercategoryconfig

interface UserCategoryConfigRepository {

    suspend fun findAll(): Result<List<UserCategoryConfig>>

    suspend fun findByUserId(userId: String): Result<List<UserCategoryConfig>>
}
