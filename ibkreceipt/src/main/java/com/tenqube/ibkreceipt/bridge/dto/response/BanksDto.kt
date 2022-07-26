package com.tenqube.ibkreceipt.bridge.dto.response

import com.tenqube.visualbase.domain.card.Card

data class BanksDto(
    val banks: List<BankDto>
)

data class BankDto(
    val id: String,
    val name: String,
    val symbol: String
) {
    companion object {
        fun fomDomain(card: Card): BankDto {
            return BankDto(
                card.id,
                card.name,
                ""
            )
        }
    }
}
