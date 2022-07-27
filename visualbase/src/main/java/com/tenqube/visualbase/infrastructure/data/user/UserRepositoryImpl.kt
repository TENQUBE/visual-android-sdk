package com.tenqube.visualbase.infrastructure.data.user

import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.infrastructure.data.user.local.UserDao
import com.tenqube.visualbase.infrastructure.data.user.local.UserModel

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {
    override suspend fun findUser(): Result<User> {
        return try {
            Result.success(
                dao.getUser()?.asDomain() ?: throw Exception("user not exists")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun save(item: User): Result<Unit> {
        return try {
            Result.success(
                dao.save(UserModel.fromDomain(item))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(item: User): Result<Unit> {
        return try {
            Result.success(
                dao.delete(UserModel.fromDomain(item))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
