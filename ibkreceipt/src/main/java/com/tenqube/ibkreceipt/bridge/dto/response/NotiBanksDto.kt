package com.tenqube.ibkreceipt.bridge.dto.response

import com.tenqube.visualbase.domain.transaction.dto.CountByNoti

data class NotiBanksDto(
    val banks: List<NotiBankDto>
)

data class NotiBankDto(
    val name: String,
    val count: Int
) {
    companion object {
        fun fromDomain(item: CountByNoti): NotiBankDto {
            return NotiBankDto(
                item.name,
                item.count
            )
        }
    }
}
