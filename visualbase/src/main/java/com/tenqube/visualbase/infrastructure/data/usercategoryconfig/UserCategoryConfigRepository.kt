package com.tenqube.visualbase.infrastructure.data.usercategoryconfig

import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigModel

class UserCategoryConfigRepositoryImpl(private val dao: UserCategoryConfigDao) : UserCategoryConfigRepository {
    override suspend fun findAll(): Result<List<UserCategoryConfig>> {
        return try {
            Result.success(
                dao.getAll().map { it.asDomain() }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun save(items: List<UserCategoryConfig>): Result<Unit> {
        return try {
            Result.success(
                items.forEach {
                    dao.insertAll(UserCategoryConfigModel.fromDomain(it))
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}