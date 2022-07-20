package com.tenqube.visualbase.infrastructure.data.usercategoryconfig

import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao

class UserCategoryConfigRepositoryImpl(private val dao: UserCategoryConfigDao) : UserCategoryConfigRepository {
    override suspend fun findAll(): Result<List<UserCategoryConfig>> {
        TODO("Not yet implemented")
    }

    override suspend fun findByUserId(userId: String): Result<List<UserCategoryConfig>> {
        TODO("Not yet implemented")
    }
}