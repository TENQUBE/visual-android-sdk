package com.tenqube.visualbase.domain.transaction

import com.tenqube.visualbase.service.transaction.dto.JoinedTransaction

interface TransactionService {
    suspend fun saveAll(items: List<JoinedTransaction>)
}
