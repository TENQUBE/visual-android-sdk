package com.tenqube.visualbase.service.transaction

import com.tenqube.visualbase.domain.card.CardRepository
import com.tenqube.visualbase.domain.category.CategoryRepository
import com.tenqube.visualbase.domain.parser.ParsedTransaction
import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.transaction.TransactionRepository
import com.tenqube.visualbase.domain.transaction.command.SaveTransactionDto
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfigRepository
import com.tenqube.visualbase.service.user.UserAppService



class TransactionAppService(
    private val transactionRepository: TransactionRepository,
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository,
    private val userAppService: UserAppService,
    private val userCategoryConfigRepository: UserCategoryConfigRepository
) {
    suspend fun getTransactions(): Result<List<JoinedTransaction>> {
        val user = userAppService.getUser()
        val transactions = transactionRepository.findAll().getOrDefault(listOf())
        val cards = cardRepository.findAll().getOrDefault(listOf())
        val categories = categoryRepository.findAll().getOrDefault(listOf())
        val userCategories = userCategoryConfigRepository.findAll().getOrDefault(listOf())

        return Result.success(listOf())
    }

    suspend fun saveTransactions(transactions: List<SaveTransactionDto>): Result<Unit> {
        return Result.success(Unit)
    }
}