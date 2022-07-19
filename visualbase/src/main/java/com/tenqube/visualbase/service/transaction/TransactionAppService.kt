package com.tenqube.visualbase.service.transaction

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.Category
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.domain.transaction.command.SaveTransactionDto
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter
import com.tenqube.visualbase.service.user.UserAppService

class TransactionAppService(
    private val transactionRepository: TransactionRepository,
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository,
    private val userCategoryConfigRepository: UserCategoryConfigRepository
) {
    suspend fun getTransactions(filter: TransactionFilter): Result<List<JoinedTransaction>> {
        val transactions = transactionRepository
            .findByFilter(filter)
            .getOrDefault(listOf())
        val cards = cardRepository
            .findAll()
            .getOrDefault(listOf())
        val categories = categoryRepository
            .findAll()
            .getOrDefault(listOf())
        val userCategories = userCategoryConfigRepository
            .findAll()
            .getOrDefault(listOf())
        val results = joinTransactions(
            transactions,
            cards,
            categories,
            userCategories
        )
        return Result.success(results)
    }

    private fun joinTransactions(
        transactions: List<Transaction>,
        cards: List<Card>,
        categories: List<Category>,
        userCategories: List<UserCategoryConfig>
    ): List<JoinedTransaction> {
        val cardMap = cards.associateBy { it.id }
        val categoryMap = categories.associateBy { it.id }
        val userCateMap = userCategories.associateBy { it.id }

        return transactions.mapNotNull {
            val card = cardMap[it.cardId]
            val category = categoryMap[it.categoryId]
            val userCategory = userCateMap[it.userCategoryConfigId]
            if(hasMandatory(card, category, userCategory)) {
                JoinedTransaction(
                    it,
                    card!!,
                    category!!,
                    userCategory!!
                )
            } else {
                null
            }
        }
    }

    private fun hasMandatory(
        card: Card?,
        category: Category?,
        userCategory: UserCategoryConfig?
    ): Boolean {
        return card != null && category != null && userCategory != null
    }

    suspend fun saveTransactions(transactions: List<SaveTransactionDto>): Result<Unit> {
        return Result.success(Unit)
    }
}
