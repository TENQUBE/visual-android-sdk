package com.tenqube.visualbase.service.card

import com.tenqube.visualbase.domain.card.Card
import com.tenqube.visualbase.domain.card.CardRepository

class CardAppService(
    private val cardRepository: CardRepository
) {
    suspend fun getCards(): Result<List<Card>> {
        return cardRepository.findAll()
    }
}