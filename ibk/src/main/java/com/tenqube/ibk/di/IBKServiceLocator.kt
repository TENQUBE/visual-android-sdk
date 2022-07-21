package com.tenqube.ibk.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibk.VisualViewModel
import com.tenqube.shared.prefs.SharedPreferenceStorage
import com.tenqube.visualbase.infrastructure.adapter.parser.ParserServiceImpl
import com.tenqube.visualbase.infrastructure.adapter.parser.rcs.RcsService
import com.tenqube.visualbase.infrastructure.adapter.parser.rule.ParsingRuleService
import com.tenqube.visualbase.infrastructure.data.card.CardRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.category.CategoryRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.transaction.TransactionRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.user.UserRepositoryImpl
import com.tenqube.visualbase.infrastructure.data.usercategoryconfig.UserCategoryConfigRepositoryImpl
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import com.tenqube.visualbase.service.parser.ParserAppService
import com.tenqube.visualbase.service.resource.ResourceAppService
import com.tenqube.webui.UIServiceBuilder

object IBKServiceLocator {
    fun provideVisualViewModel(context: Context): ViewModelProvider.Factory {
        val prefStorage = SharedPreferenceStorage(context)
        val db = ServiceLocator.provideVisualDatabase(context)
        val userDao = db.userDao()
        val cardDao = db.cardDao()
        val categoryDao = db.categoryDao()
        val transactionDao = db.transactionDao()
        val userCategoryConfigDao = db.userCategoryConfigDao()

        val userRepository = UserRepositoryImpl(userDao)
        val cardRepository = CardRepositoryImpl(cardDao)
        val categoryRepository = CategoryRepositoryImpl(categoryDao)
        val transactionRepository = TransactionRepositoryImpl(transactionDao)
        val userCategoryRepository = UserCategoryConfigRepositoryImpl(userCategoryConfigDao)
        val uiService = UIServiceBuilder().build()

        val transactionAppService = ServiceLocator.provideTransactionAppService(
            transactionRepository,
            cardRepository,
            categoryRepository,
            userCategoryRepository
        )
        val parserAppService = ServiceLocator.provideParserAppService(
            context,
            transactionAppService,
            prefStorage
        )
        return VisualViewModel.Factory(
            userAppService = ServiceLocator.provideUserAppService(
                userRepository,
                categoryRepository,
                userCategoryRepository,
                cardRepository
            ),
            transactionAppService = transactionAppService,
            cardAppService = ServiceLocator.provideCardAppService(
                cardRepository
            ),
            uiService = uiService,
            bulkParserAppService = ServiceLocator.provideBulkParserAppService(parserAppService)
        )
    }
}