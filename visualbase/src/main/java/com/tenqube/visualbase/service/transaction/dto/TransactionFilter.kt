package com.tenqube.visualbase.service.transaction.dto

import com.tenqube.shared.util.Utils
import java.util.*

data class TransactionFilter(
    val year: Int,
    val month: Int
) {
    fun toQuery(): List<String> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, 1)
        val from = "${Utils.convertCalendarToDateStr(calendar)} 00:00:00"

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val to = "${Utils.convertCalendarToDateStr(calendar)} 00:00:00"

        return listOf(
            from,
            to
        )
    }
}
