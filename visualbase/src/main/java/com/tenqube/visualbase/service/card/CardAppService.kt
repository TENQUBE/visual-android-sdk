package com.tenqube.visualbase.service.card

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository

class CardAppService(
    private val cardRepository: CardRepository
) {
    suspend fun getCards(): Result<List<Card>> {
        return try {
            Result.success(cardRepository.findAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
