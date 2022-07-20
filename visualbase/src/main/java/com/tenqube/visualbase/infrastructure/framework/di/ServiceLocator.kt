package com.tenqube.visualbase.infrastructure.framework.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyDao
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryModel
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.user.local.UserDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.framework.db.VisualDatabase
import com.tenqube.visualbase.infrastructure.framework.db.category.CategoryGeneroator
import com.tenqube.visualbase.infrastructure.framework.db.currency.CurrencyGenerator
import com.tenqube.visualbase.service.card.CardAppService
import com.tenqube.visualbase.service.parser.BulkParserAppService
import com.tenqube.visualbase.service.parser.ParserAppService
import com.tenqube.visualbase.service.transaction.TransactionAppService
import com.tenqube.visualbase.service.user.UserAppService
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private var parserAppService: ParserAppService? = null
    private var userAppService: UserAppService? = null
    private var transactionAppService: TransactionAppService? = null
    private var cardAppService: CardAppService? = null
    private var bulkParserAppService: BulkParserAppService? = null

    fun provideParserAppService(): ParserAppService {
        return parserAppService ?: createParserAppService()
    }

    private fun createParserAppService(): ParserAppService {
        return parserAppService!! // TODO 생성 모듈 만들기
    }

    fun provideUserAppService(): UserAppService {
        return userAppService?: createUserAppService()
    }

    private fun createUserAppService(): UserAppService {
        return userAppService!!
    }

    fun provideTransactionAppService(): TransactionAppService {
        return transactionAppService?: createTransactionAppService()
    }

    private fun createTransactionAppService(): TransactionAppService {
        return transactionAppService!!
    }

    fun provideCardAppService(): CardAppService {
        return cardAppService?: createCardAppService()
    }

    private fun createCardAppService(): CardAppService {
        return cardAppService!!
    }

    fun provideBulkParserAppService(): BulkParserAppService {
        return bulkParserAppService?: createBulkParserAppService()
    }

    private fun createBulkParserAppService(): BulkParserAppService {
        return bulkParserAppService!!
    }

    fun provideVisualDatabase(context: Context): VisualDatabase {
        return Room.databaseBuilder(
            context,
            VisualDatabase::class.java, "visual-ibk-db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                runBlocking {
                    val visualDb = provideVisualDatabase(context)
                    val categoryDao = visualDb.categoryDao()
                    CategoryGeneroator.generate(context).forEach {
                        categoryDao.insertAll(CategoryModel.fromDomain(it))
                    }
                    val currencyDao = visualDb.currencyDao()
                    CurrencyGenerator.generate(context).forEach {
                        currencyDao.save(it)
                    }
                }
            }
        })
        .build()
    }

    fun provideUserDao(db: VisualDatabase): UserDao {
        return db.userDao()
    }

    fun provideCurrencyDao(db: VisualDatabase): CurrencyDao {
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
