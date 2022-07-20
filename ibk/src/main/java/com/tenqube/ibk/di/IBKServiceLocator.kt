package com.tenqube.ibk.di

import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibk.VisualViewModel
import com.tenqube.visualbase.infrastructure.framework.di.ServiceLocator
import com.tenqube.webui.UIServiceBuilder

object IBKServiceLocator {
    fun provideVisualViewModel(): ViewModelProvider.Factory {
        val uiService = UIServiceBuilder().build()


        return VisualViewModel.Factory(
            userAppService = ServiceLocator.provideUserAppService(),
            transactionAppService = ServiceLocator.provideTransactionAppService(),
            cardAppService = ServiceLocator.provideCardAppService(),
            uiService = uiService,
            bulkParserAppService = ServiceLocator.provideBulkParserAppService()
        )
    }
}