package com.tenqube.visualbase.service.parser

import com.tenqube.visualbase.domain.currency.CurrencyRequest
import com.tenqube.visualbase.domain.currency.CurrencyService
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.parser.ParserService
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.search.SearchRequest
import com.tenqube.visualbase.domain.search.SearchResult
import com.tenqube.visualbase.domain.search.SearchService
import com.tenqube.visualbase.domain.search.TranCompany
import com.tenqube.visualbase.domain.transaction.command.SaveTransactionDto
import com.tenqube.visualbase.service.transaction.TransactionAppService

class ParserAppService(
    private val parserService: ParserService,
    private val currencyService: CurrencyService,
    private val searchService: SearchService,
    private val transactionAppService: TransactionAppService
) {
    suspend fun parse(sms: SMS): Result<Unit> {
        val parsedTransactions = parserService.parse(sms)
        val searchedTransactions = getSearchedTransactions(parsedTransactions)
        val currencyTransactions = calculateCurrency(searchedTransactions)
        transactionAppService.saveTransactions(
            currencyTransactions.map {
                it.asDomain(sms)
            }
        )
        return Result.success(Unit)
    }

    private suspend fun getSearchedTransactions(parsedTransactions: List<ParsedTransaction>):
            List<SearchedTransaction> {
        val searchResults = searchService.search(
            parsedTransactions.map {
                SearchRequest.from()
                (
                    it.transaction.identifier,
                    it.transaction.keyword,
                    it.transaction.spentMoney,
                    it.transaction.currency
                )
            }
        ).associateBy { it.identifier }

        return parsedTransactions.map {
            SearchedTransaction(it, getOrDefault(searchResults, it))
        }
    }

    private fun getOrDefault(
        searchResults: Map<String, SearchResult>,
        transaction: ParsedTransaction
    ) = (searchResults[transaction.transaction.identifier]
        ?: TranCompany.getDefaultTranCompany(transaction.transaction.identifier, transaction.transaction.dwType))

    private suspend fun calculateCurrency(searchedTransactions: List<SearchedTransaction>):
            List<CurrencyTransaction> {
        return searchedTransactions.map {
            CurrencyTransaction(
                it, currencyService.calculate(
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
    val searchResult: SearchResult
) {
    fun getTransaction(): tenqube.parser.model.Transaction {
        return parsedTransaction.transaction
    }
}

data class CurrencyTransaction(
    val searchedTransaction: SearchedTransaction,
    val amount: Double
) {
    fun asDomain(sms: SMS
    ): SaveTransactionDto {
        return SaveTransactionDto(
            id = this.searchedTransaction.getTransaction().identifier,
            cardName = this.searchedTransaction.getTransaction().cardName,
            cardType = this.searchedTransaction.getTransaction().cardType,
            cardSubType = this.searchedTransaction.getTransaction().cardSubType,
            company = this.searchedTransaction.searchResult.company,
            categoryCode = this.searchedTransaction.searchResult.categoryCode,
            spentDate = this.searchedTransaction.getTransaction().spentDate,
            finishDate = this.searchedTransaction.getTransaction().finishDate,
            spentMoney = amount,
            oriSpentMoney = this.searchedTransaction.getTransaction().spentMoney,
            installmentCnt = this.searchedTransaction.getTransaction().installmentCount,
            keyword = this.searchedTransaction.getTransaction().keyword,
            currency = this.searchedTransaction.getTransaction().currency,
            dwType = this.searchedTransaction.getTransaction().dwType,
            memo = this.searchedTransaction.getTransaction().memo,
            sms = sms
        )
    }
}