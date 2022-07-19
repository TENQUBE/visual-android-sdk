package com.tenqube.visualbase.infrastructure.data.usercategoryconfig

import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository

class UserCategoryConfigRepositoryImpl : UserCategoryConfigRepository {
    override suspend fun findAll(): Result<List<UserCategoryConfig>> {
        TODO("Not yet implemented")
    }

    override suspend fun findByUserId(userId: String): Result<List<UserCategoryConfig>> {
        TODO("Not yet implemented")
    }
}