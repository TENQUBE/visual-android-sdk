package com.tenqube.visualbase.infrastructure.data.user

import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun findUser(): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(item: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(item: User): Result<Unit> {
        TODO("Not yet implemented")
    }
}