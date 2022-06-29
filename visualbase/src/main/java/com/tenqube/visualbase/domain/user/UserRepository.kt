package com.tenqube.visualbase.domain.user

interface UserRepository {

    suspend fun findUser(): Result<User>

    suspend fun findById(id: String): Result<User>

    suspend fun findAll(): Result<List<User>>

    suspend fun save(item: User): Result<Unit>

    suspend fun delete(item: User): Result<Unit>

}