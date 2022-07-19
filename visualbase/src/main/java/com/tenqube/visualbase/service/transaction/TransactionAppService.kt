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
import java.util.*

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

    suspend fun saveTransactions(items: List<SaveTransactionDto>): Result<Unit> {
        val cardMap = saveCards(items)
            .associateBy { it.getUniqueKey()}

        val categoryMap = categoryRepository
            .findAll()
            .getOrDefault(listOf())
            .associateBy { it.code.toString() }

        val userCateMap = userCategoryConfigRepository
            .findAll()
            .getOrDefault(listOf())
            .associateBy { it.code.toString() }

        val transactions = items.mapNotNull {
            val card = cardMap[it.getUniqueCardKey()]
            val category = categoryMap[it.categoryCode]
            val userCategory = userCateMap[it.categoryCode.substring(0, 2)]

            if(hasMandatory(card, category, userCategory)) {
                Transaction(
                    id = it.id,
                    categoryId = category!!.id,
                    cardId = card!!.id,
                    userCategoryConfigId = userCategory!!.id,
                    company = it.company,
                    spentDate = it.spentDate,
                    finishDate = it.finishDate,
                    spentMoney = it.spentMoney,
                    oriSpentMoney = it.oriSpentMoney,
                    installmentCnt = it.installmentCnt,
                    keyword = it.keyword,
                    currency = it.currency,
                    dwType = it.dwType,
                    memo = it.memo
                )
            } else {
                null
            }
        }
        transactionRepository.saveAll(transactions)

        return Result.success(Unit)
    }

    private suspend fun saveCards(items: List<SaveTransactionDto>) : List<Card> {
        val cardMap = cardRepository
            .findAll()
            .getOrDefault(listOf())
            .associateBy { it.getUniqueKey() }

        val alreadyExistCards = items.mapNotNull {
            cardMap[it.getUniqueCardKey()]
        }

        val newCards = items.mapNotNull {
            if (cardMap[it.getUniqueCardKey()] == null) {
                Card(
                    UUID.randomUUID().toString(),
                    "",
                    it.cardName,
                    it.cardType,
                    it.cardSubType,
                    it.cardName,
                    it.cardType,
                    it.cardSubType
                )
            } else {
                null
            }
        }
        cardRepository.save(newCards)
        return newCards + alreadyExistCards
    }
}
