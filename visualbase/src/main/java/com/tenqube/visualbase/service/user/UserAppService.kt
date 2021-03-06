package com.tenqube.visualbase.service.user

import android.content.Context
import com.tenqube.shared.error.UserAlreadyExistException
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.shared.util.Constants
import com.tenqube.visualbase.domain.auth.AuthService
import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.notification.NotificationApp
import com.tenqube.visualbase.domain.notification.NotificationService
import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.domain.user.command.CreateUser
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserRequestDto
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.dto.UserResultDto
import com.tenqube.visualbase.infrastructure.framework.db.category.CategoryGeneroator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UserAppService(
    private val context: Context,
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val userCategoryConfigRepository: UserCategoryConfigRepository,
    private val cardRepository: CardRepository,
    private val currencyService: CurrencyService,
    private val prefStorage: PrefStorage,
    private val notificationService: NotificationService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getNotiApps(): Result<List<NotificationApp>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(notificationService.getNotifications())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setNotiEnabled(enabled: Boolean) = withContext(ioDispatcher) {
        notificationService.setNotiEnabled(enabled)
    }

    suspend fun signUp(request: CreateUser): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
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
        prefStorage.accessToken = userResultDto.authorization.sdk
        prefStorage.refreshToken = userResultDto.authorization.sdk
        prefStorage.resourceUrl = userResultDto.resource.url
        prefStorage.resourceApiKey = userResultDto.resource.apiKey
        prefStorage.searchUrl = userResultDto.search.url
        prefStorage.searchApiKey = userResultDto.search.apiKey
    }

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
                Card(
                    "visual_cash_id",
                    user.id,
                    "??????",
                    Constants.CardType.CASH.ordinal,
                    0,
                    "??????",
                    Constants.CardType.CASH.ordinal,
                    0
                )
            )
        )
    }

    private suspend fun checkNewUserOrThrow() {
        val user = userRepository.findUser().getOrNull()
        if (user != null) {
            throw UserAlreadyExistException("user already exist")
        }
    }

    suspend fun getUser(): User = withContext(ioDispatcher) {
        return@withContext userRepository.findUser().getOrThrow()
    }
}
