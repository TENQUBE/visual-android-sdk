package com.tenqube.visualbase.service.user

import android.security.keystore.UserNotAuthenticatedException
import com.tenqube.shared.util.Constants
import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.domain.user.command.CreateUser
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import java.util.*

class UserAppService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val userCategoryConfigRepository: UserCategoryConfigRepository,
    private val cardRepository: CardRepository
) {
    suspend fun signUp(request: CreateUser): Result<Unit> {
        return try {
            checkNewUserOrThrow()
            val user = User.from(request)
            userRepository.save(user)
            saveCategoryConfig(user)
            saveCard(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveCategoryConfig(user: User) {
        val categories = categoryRepository.findAll().getOrDefault(listOf())
        val largeCategoryMap = categories.groupBy { it.code.substring(0, 2) }
        userCategoryConfigRepository.save(
            largeCategoryMap.keys.toList().map {
                UserCategoryConfig(
                    UUID.randomUUID().toString(),
                    user.id,
                    it,
                    "large",
                    isExcept = it == "88" || it == "98",
                    isMain = false
                )
            }
        )
    }

    private suspend fun saveCard(user: User) {
        cardRepository.save(
            listOf(
                Card("visual_cash_id",
                    user.id,
                    "현금",
                    Constants.CardType.CASH.ordinal,
                    0,
                    "현금",
                    Constants.CardType.CASH.ordinal,
                    0
                )
            )
        )
    }

    private suspend fun checkNewUserOrThrow() {
        val user = userRepository.findUser().getOrNull()
        if (user != null) {
            throw UserNotAuthenticatedException("user already exist")
        }
    }

    suspend fun getUser(): User {
        return userRepository.findUser().getOrThrow()
    }
}
