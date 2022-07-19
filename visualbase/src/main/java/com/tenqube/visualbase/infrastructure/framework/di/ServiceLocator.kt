package com.tenqube.visualbase.infrastructure.framework.di

import android.content.Context
import androidx.room.Room
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyDao
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.user.local.UserDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.framework.db.VisualDatabase
import com.tenqube.visualbase.service.parser.ParserAppService

object ServiceLocator {

    private var parserAppService: ParserAppService? = null

    fun provideParserAppService(): ParserAppService {
        return parserAppService ?: createParserAppService()
    }

    private fun createParserAppService(): ParserAppService {
        return parserAppService!! // TODO 생성 모듈 만들기
    }

    fun provideVisualDatabase(context: Context): VisualDatabase {
        return Room.databaseBuilder(
            context,
            VisualDatabase::class.java, "visual-ibk-db"
        ).build()
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
