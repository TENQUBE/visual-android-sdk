package com.tenqube.visualbase.domain.search

import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.transaction.Company

interface SearchService {
    suspend fun search(request: SearchRequest): SearchResult
}

data class SearchRequest(
    val transactions: List<SearchTransaction>
)
data class SearchTransaction(
    val identifier: String,
    val keyword: String,
    val type: String,
    val at: String,
    val method: String,
    val amount: Double,
    val amountType: String,
    val lat: Double,
    val long: Double,
    val lCode: Int,
    val mCode: Int
) {
    companion object {
        fun from(transaction: ParsedTransaction) : SearchTransaction {
            return SearchTransaction(
                identifier = transaction.transaction.identifier,
                keyword = transaction.transaction.keyword,
                type = getType(transaction.transaction.dwType),
                at = transaction.transaction.spentDate,
                method = getMethod(transaction.transaction.cardType),
                amount = transaction.transaction.spentMoney,
                amountType = getAmountType(transaction.transaction.currency),
                lat = 0.0,
                long = 0.0,
                lCode = 0,
                mCode = 0
            )
        }

        private fun getType(dwType: Int) : String {
            return when(dwType) {
                0 -> "deposit"
                else -> "withdraw"
            }
        }

        private fun getMethod(cardType: Int): String {
            return when(cardType) {
                0 -> "debit"
                1 -> "credit"
                else -> "account"
            }
        }

        private fun getAmountType(currency: String): String {
            return if(currency.isEmpty() || "none" == currency) {
                "KRW"
            } else {
                currency
            }
        }
    }
}

data class SearchResult(
    val results: List<TranCompany>
)

data class TranCompany(
    val identifier: String,
    val classCode: String,
    val company: Company,
    val category: SearchCategory,
    val keyword: Keyword
) {
    companion object {
        fun getDefaultTranCompany(currentTran: SearchTransaction): TranCompany {
            val isDeposit = currentTran.type == "deposit"
            val isBank = currentTran.method == "account"
            val keyword = Keyword(currentTran.keyword, currentTran.keyword)
            val identifier = currentTran.identifier
            val company: Company
            val category: SearchCategory
            val classCode: String
            if (isDeposit) {
                company = Company("3983462", "기타", "")
                category = SearchCategory("901010")
                classCode = "DP"
            } else {
                if (isBank) {
                    company = Company("3983436", "기타", "")
                    category = SearchCategory("841010")
                    classCode = "WFDW"
                } else {
                    company = Company("1260117", "미분류", "")
                    category = SearchCategory("101010")
                    classCode = "NF"
                }
            }
            return TranCompany(identifier, classCode, company, category, keyword)
        }
    }
}

data class SearchCategory(
    val code: String
)

data class Keyword(
    val ori: String,
    val search: String
)