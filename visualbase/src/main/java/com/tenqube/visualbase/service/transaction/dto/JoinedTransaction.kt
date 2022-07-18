package com.tenqube.visualbase.service.transaction.dto

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.category.Category
import com.tenqube.visualbase.domain.transaction.Transaction
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig

data class JoinedTransaction(
    val transaction: Transaction,
    val card: Card,
    val category: Category,
    val userCategoryConfig: UserCategoryConfig
)
