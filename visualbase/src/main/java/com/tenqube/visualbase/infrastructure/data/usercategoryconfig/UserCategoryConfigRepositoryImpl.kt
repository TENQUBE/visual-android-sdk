package com.tenqube.visualbase.infrastructure.data.usercategoryconfig

import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigModel

class UserCategoryConfigRepositoryImpl(private val dao: UserCategoryConfigDao) :
    UserCategoryConfigRepository {
    override suspend fun findAll(): List<UserCategoryConfig> {
        return dao.getAll().map { it.asDomain() }
    }

    override suspend fun findById(id: String): UserCategoryConfig? {
        return dao.getById(id)?.asDomain()
    }

    override suspend fun save(items: List<UserCategoryConfig>) {
        items.forEach {
            dao.insertAll(UserCategoryConfigModel.fromDomain(it))
        }
    }
}
