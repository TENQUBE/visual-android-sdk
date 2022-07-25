package com.tenqube.ibk.bridge.dto.response

import com.tenqube.visualbase.service.transaction.dto.CountByCard

data class NotiBanksDto(
    val banks: List<NotiBankDto>
)

data class NotiBankDto(
    val name: String,
    val count: Int
) {
    companion object {
        fun fromDomain(item: CountByCard): NotiBankDto {
            return NotiBankDto(
                item.name,
                item.count
            )
        }
    }
}
