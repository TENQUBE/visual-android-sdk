package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.currency.CurrencyRequest
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.parser.SmsFilter
import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchService
import com.tenqube.visualbase.domain.search.SearchTransaction
import com.tenqube.visualbase.domain.search.TranCompany
import com.tenqube.visualbase.domain.transaction.dto.SaveTransactionDto
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.notification.NotificationService
import com.tenqube.visualbase.domain.notification.dto.NotificationDto
import com.tenqube.visualbase.service.transaction.TransactionAppService
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction
import java.util.*

class ParserAppService(
    private val parserService: ParserService,
    val currencyService: CurrencyService,
    private val searchService: SearchService,
    private val transactionAppService: TransactionAppService,
    private val prefStorage: PrefStorage,
    private val notificationService: NotificationService
) {
    suspend fun parseBulk(adapter: BulkAdapter) {
        parserService.parseBulk(adapter)
    }

    suspend fun parseRcsListAfterLastParsedTime() {
        val lastTime = prefStorage.lastRcsTime
        val currentTime = Calendar.getInstance().timeInMillis
        val smsList = parserService.getRcsList(SmsFilter(lastTime, currentTime))
        smsList.forEach {
            parse(it)
        }
        prefStorage.lastRcsTime = currentTime
    }

    suspend fun parse(sms: SMS): Result<Unit> {
        return try {
            val parsedTransactions = parserService.parse(sms)
            if(parsedTransactions.isNotEmpty()) {
                saveTransactions(parsedTransactions)
                parsedTransactions.firstOrNull { it.transaction.isCurrentTran }?.let {
                    val transaction = transactionAppService.getByIdentifier(it.transaction.identifier)
                        .getOrThrow()
                    showNotification(transaction)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun showNotification(transaction: JoinedTransaction) {
        notificationService.show(
            NotificationDto(
                transaction
            )
        )
    }

    suspend fun saveTransactions(
        parsedTransactions: List<ParsedTransaction>
    ): Result<Unit> {
        val searchedTransactions = getSearchedTransactions(parsedTransactions)
        val currencyTransactions = calculateCurrency(searchedTransactions)
        transactionAppService.saveTransactions(
            currencyTransactions.map {
                it.asDomain()
            }
        )

        return Result.success(Unit)
    }

    suspend fun getSmsList(filter: SmsFilter): List<SMS> {
        val sms = parserService.getSmsList(filter)
        val rcs = parserService.getRcsList(filter).also {
            prefStorage.lastRcsTime = filter.toAt
        }
        return (sms + rcs).sortedBy { it.smsId }
    }

    private suspend fun getSearchedTransactions(parsedTransactions: List<ParsedTransaction>):
        List<SearchedTransaction> {
        val searchRequests = SearchRequest.from(parsedTransactions)
        val searchResults = searchService.search(
            searchRequests
        ).results.associateBy { it.identifier }

        val searchRequestMap = searchRequests.transactions.associateBy { it.identifier }
        return parsedTransactions.mapNotNull {
            searchRequestMap[it.transaction.identifier]?.let { request ->
                SearchedTransaction(it, getOrDefault(searchResults, request))
            }
        }
    }

    private fun getOrDefault(
        searchResults: Map<String, TranCompany>,
        searchTransaction: SearchTransaction
    ) = (
        searchResults[searchTransaction.identifier]
            ?: TranCompany.getDefaultTranCompany(searchTransaction)
        )

    private suspend fun calculateCurrency(searchedTransactions: List<SearchedTransaction>):
        List<CurrencyTransaction> {
        return searchedTransactions.map {
            CurrencyTransaction(
                it,
                currencyService.exchange(
                    CurrencyRequest(
                        from = it.parsedTransaction.transaction.currency,
                        amount = it.parsedTransaction.transaction.spentMoney
                    )
                )
            )
        }
    }
}

data class SearchedTransaction(
    val parsedTransaction: ParsedTransaction,
    val searchResult: TranCompany
) {
    fun getTransaction(): tenqube.parser.model.Transaction {
        return parsedTransaction.transaction
    }
}

data class CurrencyTransaction(
    val searchedTransaction: SearchedTransaction,
    val amount: Double
) {
    fun asDomain(): SaveTransactionDto {
        return SaveTransactionDto(
            id = this.searchedTransaction.getTransaction().identifier,
            cardName = this.searchedTransaction.getTransaction().cardName,
            cardType = this.searchedTransaction.getTransaction().cardType,
            cardSubType = this.searchedTransaction.getTransaction().cardSubType,
            company = this.searchedTransaction.searchResult.company,
            categoryCode = this.searchedTransaction.searchResult.category.code,
            spentDate = this.searchedTransaction.getTransaction().spentDate,
            finishDate = this.searchedTransaction.getTransaction().finishDate,
            spentMoney = amount,
            oriSpentMoney = this.searchedTransaction.getTransaction().spentMoney,
            installmentCnt = this.searchedTransaction.getTransaction().installmentCount,
            keyword = this.searchedTransaction.getTransaction().keyword,
            currency = this.searchedTransaction.getTransaction().currency,
            dwType = this.searchedTransaction.getTransaction().dwType,
            memo = this.searchedTransaction.getTransaction().memo,
            sms = SMS(
                this.searchedTransaction.getTransaction().smsId,
                this.searchedTransaction.getTransaction().fullSms,
                "com.kakao.talk",
                "com.kakao.talk",
                this.searchedTransaction.getTransaction().smsDate,
                0
            )
        )
    }
}
