package com.tenqube.visualbase.infrastructure.adapter.currency

import android.content.Context
import com.tenqube.visualbase.domain.currency.CurrencyRequest
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.util.getValue
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyDao
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyModel
import com.tenqube.visualbase.infrastructure.adapter.currency.remote.CurrencyRemoteDataSource
import com.tenqube.visualbase.infrastructure.framework.db.currency.CurrencyGenerator

class CurrencyServiceImpl(
    private val context: Context,
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val currencyDao: CurrencyDao
) : CurrencyService {

    override suspend fun exchange(request: CurrencyRequest): Double {
        if (request.isKorea()) {
            return 1.0
        }

        val rate = currencyDao.findByFromAndTo(request.from, request.to)?.let {
            getFromLocal(request, it)
        } ?: getFromRemote(request).also {
            currencyDao.save(
                CurrencyModel(request.from, request.to, it, System.currentTimeMillis())
            )
        }

        return request.amount * rate.toDouble()
    }

    override suspend fun prepopulate() {
        CurrencyGenerator.generate(context).forEach {
            currencyDao.save(it)
        }
    }

    private suspend fun getFromRemote(request: CurrencyRequest): Float {
        return currencyRemoteDataSource.exchange(request).getValue().rate
    }

    private suspend fun getFromLocal(request: CurrencyRequest, currency: CurrencyModel): Float {
        return if ((System.currentTimeMillis() - currency.createdAt) > 24 * 60 * 60 * 1000) {
            getFromRemote(request).also {
                currencyDao.update(
                    currency.copy(rate = it, createdAt = System.currentTimeMillis())
                )
            }
        } else {
            currency.rate
        }
    }
}
