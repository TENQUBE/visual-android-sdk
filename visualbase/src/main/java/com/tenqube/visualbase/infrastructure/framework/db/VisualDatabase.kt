package com.tenqube.visualbase.infrastructure.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyDao
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyModel
import com.tenqube.visualbase.infrastructure.data.card.local.CardDao
import com.tenqube.visualbase.infrastructure.data.card.local.CardModel
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryDao
import com.tenqube.visualbase.infrastructure.data.category.local.CategoryModel
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionDao
import com.tenqube.visualbase.infrastructure.data.transaction.local.TransactionModel
import com.tenqube.visualbase.infrastructure.data.user.local.UserDao
import com.tenqube.visualbase.infrastructure.data.user.local.UserModel
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigDao
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local.UserCategoryConfigModel

@Database(entities = [
    CurrencyModel::class,
    UserModel::class,
    CardModel::class,
    CategoryModel::class,
    UserCategoryConfigModel::class,
    TransactionModel::class],
    version = 1)
abstract class VisualDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userCategoryConfigDao(): UserCategoryConfigDao
    abstract fun transactionDao(): TransactionDao
}