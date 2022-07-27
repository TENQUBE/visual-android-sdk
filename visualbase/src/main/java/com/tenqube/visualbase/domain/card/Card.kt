package com.tenqube.visualbase.domain.card

import com.tenqube.visualbase.domain.Entity

data class Card(
    override val id: String,
    val userId: String,
    val name: String,
    val type: Int,
    val subType: Int = 0,
    val displayName: String,
    val displayType: Int = 0,
    val displaySubType: Int = 0,
    val billingDay: Int = 1,
    val balance: Double = 0.0,
    val memo: String = "",
    val isExcept: Boolean = false,
    val isCustom: Boolean = false,
    val isDeleted: Boolean = false
) : Entity {
    fun getUniqueKey(): String {
        return "${name}${type}$subType}"
    }
}
