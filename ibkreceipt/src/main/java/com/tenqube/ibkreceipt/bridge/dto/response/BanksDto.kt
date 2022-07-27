package com.tenqube.ibkreceipt.bridge.dto.response

import com.tenqube.visualbase.domain.notification.NotificationApp

data class BanksDto(
    val banks: List<BankDto>
)

data class BankDto(
    val id: String,
    val name: String,
    val symbol: String
) {
    companion object {
        fun fomDomain(app: NotificationApp): BankDto {
            return BankDto(
                app.id,
                app.name,
                app.image
            )
        }
    }
}
