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
import com.tenqube.visualbase.domain.transaction.command.SaveTransactionDto
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.service.transaction.TransactionAppService
import java.util.*

class ParserAppService(
    private val parserService: ParserService,
    private val currencyService: CurrencyService,
    private val searchService: SearchService,
    private val transactionAppService: TransactionAppService,
    private val prefStorage: PrefStorage
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
        val parsedTransactions = parserService.parse(sms)
        return saveTransactions(parsedTransactions)
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
        return parserService.getSmsList(filter) +
            parserService.getRcsList(filter).also {
                prefStorage.lastRcsTime = filter.toAt
            }
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
                this.searchedTransaction.getTransaction().sender,
                this.searchedTransaction.getTransaction().sender,
                this.searchedTransaction.getTransaction().smsDate,
                this.searchedTransaction.getTransaction().smsType
            )
        )
    }
}
