package com.tenqube.visualbase.service.user

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.shared.util.Constants
import com.tenqube.visualbase.domain.auth.AuthService
import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.domain.user.command.CreateUser
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryModel
import com.tenqube.visualbase.infrastructure.framework.db.category.CategoryGeneroator
import com.tenqube.visualbase.infrastructure.framework.db.currency.CurrencyGenerator
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import java.util.*

class UserAppService(
    private val context: Context,
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val userCategoryConfigRepository: UserCategoryConfigRepository,
    private val cardRepository: CardRepository,
    private val currencyService: CurrencyService,
    private val prefStorage: PrefStorage
) {
    suspend fun signUp(request: CreateUser): Result<Unit> {
        return try {
            checkNewUserOrThrow()
            val user = User.from(request)
            val result = authService.signUp(UserRequestDto(request.uid, ""))
            saveBaseConfig(result)
            currencyService.prepopulate()
            userRepository.save(user)
            saveCategoryConfig(user)
            saveCard(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveBaseConfig(userResultDto: UserResultDto) {
        prefStorage.accessToken = userResultDto.authorization.sdk.accessToken
        prefStorage.refreshToken = userResultDto.authorization.sdk.refreshToken
        prefStorage.resourceUrl = userResultDto.resource.url
        prefStorage.resourceApiKey = "6RAiQu9TqM9Yc0VwnsVkp8DUYrppjP7G8hemWM76"
        prefStorage.searchUrl = userResultDto.search.url
        prefStorage.searchApiKey = userResultDto.search.apiKey }

    private suspend fun saveCategoryConfig(user: User) {
        CategoryGeneroator.generate(context).let {
            categoryRepository.save(it)
        }
        val categories = categoryRepository.findAll()
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
