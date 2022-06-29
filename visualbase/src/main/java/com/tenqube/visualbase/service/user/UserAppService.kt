package com.tenqube.visualbase.service.user

import android.security.keystore.UserNotAuthenticatedException
import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.domain.user.command.CreateUser
import java.lang.Exception

class UserAppService(
    private val userRepository: UserRepository
) {
    suspend fun signUp(request: CreateUser): Result<Unit> {
        return try {
            checkNewUserOrThrow()
            userRepository.save(User.from(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun checkNewUserOrThrow() {
        val user = userRepository.findUser().getOrNull()
        if(user != null) {
            throw UserNotAuthenticatedException("user already exist")
        }
    }

    suspend fun getUser() : User {
        return userRepository.findUser().getOrThrow()
    }
}