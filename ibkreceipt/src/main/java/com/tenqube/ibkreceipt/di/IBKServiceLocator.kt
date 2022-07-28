package com.tenqube.ibkreceipt.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibkreceipt.IBKSharedPreference
import com.tenqube.ibkreceipt.VisualViewModel
import com.tenqube.shared.prefs.SharedPreferenceStorage
import com.tenqube.visualbase.infrastructure.adapter.auth.AuthServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthApi
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.notification.NotificationServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.notification.local.NotificationAppLocalDataSource
import com.tenqube.visualbase.infrastructure.data.card.CardRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.category.CategoryRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.transaction.TransactionRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.transaction.remote.TransactionApiService
import com.tenqube.visualbase.infrastructure.data.transaction.remote.TransactionRemoteDataSource
import com.tenqube.visualbase.infrastructure.data.user.UserRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.UserCategoryConfigRepositoryImpl
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import com.tenqube.webui.UIServiceBuilder

object IBKServiceLocator {
    fun provideVisualViewModel(context: AppCompatActivity): ViewModelProvider.Factory {
        val okHttpClient = ServiceLocator.provideOkHttpClient()
        val retrofit = ServiceLocator.provideRetrofit(okHttpClient)

        val prefStorage = SharedPreferenceStorage(context)
        val db = ServiceLocator.provideVisualDatabase(context)
        val userDao = ServiceLocator.provideUserDao(db)
        val cardDao = ServiceLocator.provideCardDao(db)
        val categoryDao = ServiceLocator.provideCategoryDao(db)
        val transactionDao = ServiceLocator.provideTransactionDao(db)
        val userCategoryConfigDao = ServiceLocator.provideUserCategoryConfigDao(db)

        val userRepository = UserRepositoryImpl(userDao)
        val cardRepository = CardRepositoryImpl(cardDao)
        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionApi = retrofit.create(TransactionApiService::class.java)
        val transactionRemoteDataSource = TransactionRemoteDataSource(
            transactionApi,
            prefStorage
        )

        val transactionRepository = TransactionRepositoryImpl(
            transactionDao,
            transactionRemoteDataSource
        )
        val userCategoryRepository = UserCategoryConfigRepositoryImpl(userCategoryConfigDao)
        val uiService = UIServiceBuilder()
            .activity(context)
            .refreshCallback {
            }
            .build()

        val transactionAppService = ServiceLocator.provideTransactionAppService(
            transactionRepository,
            cardRepository,
            categoryRepository,
            userCategoryRepository,
            context.packageManager
        )
        val parserAppService = ServiceLocator.provideParserAppService(
            context,
            transactionAppService,
            prefStorage,
            retrofit
        )

        val authApi = retrofit.create(AuthApi::class.java)
        val authRemote = AuthRemoteDataSource(authApi, prefStorage)
        val authService = AuthServiceImpl(authRemote)

        val notificationAppLocalDataSource = NotificationAppLocalDataSource(context)
        val notificationService = NotificationServiceImpl(
            context,
            prefStorage,
            notificationAppLocalDataSource
        )
        return VisualViewModel.Factory(
            userAppService = ServiceLocator.provideUserAppService(
                context,
                authService,
                userRepository,
                categoryRepository,
                userCategoryRepository,
                cardRepository,
                parserAppService.currencyService,
                prefStorage,
                notificationService
            ),
            transactionAppService = transactionAppService,
            uiService = uiService,
            bulkParserAppService = ServiceLocator.provideBulkParserAppService(parserAppService),
            IBKSharedPreference(context)
        )
    }
}
