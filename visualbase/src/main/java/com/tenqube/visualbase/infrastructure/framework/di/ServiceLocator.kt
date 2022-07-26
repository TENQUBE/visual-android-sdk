package com.tenqube.visualbase.infrastructure.framework.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.shared.prefs.SharedPreferenceStorage
import com.tenqube.visualbase.domain.auth.AuthService
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.resource.ResourceService
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.domain.user.UserRepository
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.infrastructure.adapter.auth.AuthServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthApi
import com.tenqube.visualbase.infrastructure.adapter.auth.remote.AuthRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.currency.CurrencyServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyDao
import com.tenqube.visualbase.infrastructure.adapter.currency.remote.CurrencyApiService
import com.tenqube.visualbase.infrastructure.adapter.currency.remote.CurrencyRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.notification.NotificationServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.notification.local.NotificationAppLocalDataSource
import com.tenqube.visualbase.infrastructure.adapter.parser.ParserServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.adapter.parser.rule.ParsingRuleService
import com.tenqube.visualbase.infrastructure.adapter.resource.ResourceServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.ResourceApiService
import com.tenqube.visualbase.infrastructure.adapter.resource.remote.ResourceRemoteDataSource
import com.tenqube.visualbase.infrastructure.adapter.search.SearchServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.search.remote.SearchApiService
import com.tenqube.visualbase.infrastructure.adapter.search.remote.SearchRemoteDataSource
import com.tenqube.visualbase.infrastructure.data.card.CardRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.category.CategoryRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao
import com.tenqube.visualbase.infrastructure.data.transaction.TransactionRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.user.local.UserDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.UserCategoryConfigRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.framework.api.dto.VisualApiConfig
import com.tenqube.visualbase.infrastructure.framework.db.VisualDatabase
import com.tenqube.visualbase.service.card.CardAppService
import com.tenqube.visualbase.service.parser.BulkParserAppService
import com.tenqube.visualbase.service.parser.ParserAppService
import com.tenqube.visualbase.service.resource.ResourceAppService
import com.tenqube.visualbase.service.transaction.TransactionAppService
import com.tenqube.visualbase.service.user.UserAppService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

    private var parserAppService: ParserAppService? = null

    fun provideOkHttpClient():
            OkHttpClient {
        return OkHttpClient.Builder().apply {
            this.connectTimeout(5, TimeUnit.SECONDS)
            this.readTimeout(5, TimeUnit.SECONDS)
            this.writeTimeout(5, TimeUnit.SECONDS)
            this.addInterceptor(
                HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(VisualApiConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun provideAuthService(
        retrofit: Retrofit,
        prefStorage: PrefStorage): AuthService {
        val api = retrofit.create(AuthApi::class.java)
        val remote = AuthRemoteDataSource(api, prefStorage)
        return AuthServiceImpl(
            remote
        )
    }

    private fun provideResourceService(retrofit: Retrofit, prefStorage: PrefStorage): ResourceService {
        val resourceApi = retrofit.create(ResourceApiService::class.java)
        val remote = ResourceRemoteDataSource(resourceApi, prefStorage)
        return ResourceServiceImpl(remote)
    }

    private fun provideResourceAppService(retrofit: Retrofit, prefStorage: PrefStorage): ResourceAppService {
        val resourceService = provideResourceService(retrofit, prefStorage)
        return ResourceAppService(resourceService)
    }

    fun provideParserAppService(context: Context) : ParserAppService {
        return parserAppService ?: createParserAppService(context).apply {
            parserAppService = this
        }
    }

    private fun createParserAppService(context: Context): ParserAppService {
        val retrofit = provideRetrofit(provideOkHttpClient())
        val db = provideVisualDatabase(context)
        val transactionDao = provideTransactionDao(db)
        val cardDao = provideCardDao(db)
        val categoryDao = provideCategoryDao(db)
        val userCategoryDao = provideUserCategoryConfigDao(db)
        val transactionRepository = TransactionRepositoryImpl(transactionDao)
        val cardRepository = CardRepositoryImpl(cardDao)
        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val userCategoryRepository = UserCategoryConfigRepositoryImpl(userCategoryDao)
        val transactionAppService = TransactionAppService(
            transactionRepository,
            cardRepository,
            categoryRepository,
            userCategoryRepository,
            context.packageManager
        )
        val prefStorage = SharedPreferenceStorage(context)
        return provideParserAppService(
            context,
            transactionAppService,
            prefStorage,
            retrofit
        )
    }

    fun provideParserAppService(context: Context,
                                transactionAppService: TransactionAppService,
                                prefStorage: PrefStorage,
                                retrofit: Retrofit) : ParserAppService {
        val resourceAppService = provideResourceAppService(retrofit, prefStorage)
        val parsingRuleService = ParsingRuleService(resourceAppService, prefStorage)

        val parser = tenqube.parser.core.ParserService.getInstance(context)
        parser.setDebugMode(true)
        val rcsService = RcsService(context)
        val parserService = ParserServiceImpl(
            context,
            parserService = parser,
            parsingRuleService = parsingRuleService,
            rcsService = rcsService)

        val db = provideVisualDatabase(context)

        // currency
        val currencyDao = provideCurrencyDao(db)
        val currencyApi = retrofit.create(CurrencyApiService::class.java)
        val currencyRemote = CurrencyRemoteDataSource(
            currencyApi,
            prefStorage
        )
        val currencyService = CurrencyServiceImpl(context, currencyRemote, currencyDao)

        // search
        val searchApi = retrofit.create(SearchApiService::class.java)
        val searchRemote = SearchRemoteDataSource(searchApi, prefStorage)
        val searchService = SearchServiceImpl(searchRemote)
        val notificationAppLocalDataSource = NotificationAppLocalDataSource(context)
        val notificationService = NotificationServiceImpl(
            context,
            prefStorage,
            notificationAppLocalDataSource
        )

        return ParserAppService(
            parserService = parserService,
            currencyService = currencyService,
            searchService = searchService,
            transactionAppService,
            prefStorage = prefStorage,
            notificationService
        )
    }

    fun provideUserAppService(
        context: Context,
        authService: AuthService,
        userRepository: UserRepository,
        categoryRepository: CategoryRepository,
        userCategoryConfigRepository: UserCategoryConfigRepository,
        cardRepository: CardRepository,
        currencyService: CurrencyService,
        prefStorage: PrefStorage
    ): UserAppService {
        return UserAppService(
            context,
            authService,
            userRepository,
            categoryRepository,
            userCategoryConfigRepository,
            cardRepository,
            currencyService,
            prefStorage
        )
    }

    fun provideTransactionAppService(
        transactionRepository: TransactionRepository,
        cardRepository: CardRepository,
        categoryRepository: CategoryRepository,
        userCategoryConfigRepository: UserCategoryConfigRepository,
        packageManager: PackageManager

    ): TransactionAppService {
        return TransactionAppService(
            transactionRepository,
            cardRepository,
            categoryRepository,
            userCategoryConfigRepository,
            packageManager
        )
    }

    fun provideCardAppService(cardRepository: CardRepository): CardAppService {
        return CardAppService(cardRepository)
    }

    fun provideBulkParserAppService(parserAppService: ParserAppService): BulkParserAppService {
        return BulkParserAppService(parserAppService)
    }

    fun provideVisualDatabase(context: Context): VisualDatabase {
        return Room.databaseBuilder(
            context,
            VisualDatabase::class.java, "visual-ibk-db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        })
        .build()
    }

    fun provideUserDao(db: VisualDatabase): UserDao {
        return db.userDao()
    }

    private fun provideCurrencyDao(db: VisualDatabase): CurrencyDao {
        return db.currencyDao()
    }

    fun provideCardDao(db: VisualDatabase): CardDao {
        return db.cardDao()
    }

    fun provideCategoryDao(db: VisualDatabase): CategoryDao {
        return db.categoryDao()
    }

    fun provideUserCategoryConfigDao(db: VisualDatabase): UserCategoryConfigDao {
        return db.userCategoryConfigDao()
    }

    fun provideTransactionDao(db: VisualDatabase): TransactionDao {
        return db.transactionDao()
    }
}
