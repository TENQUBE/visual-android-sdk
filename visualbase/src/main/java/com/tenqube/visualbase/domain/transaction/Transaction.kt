package com.tenqube.visualbase.domain.transaction

import com.tenqube.visualbase.domain.Entity
import com.tenqube.visualbase.domain.parser.SMS
import java.util.*

data class Transaction(
    override val id: String = UUID.randomUUID().toString(),
    val categoryId: String,
    val cardId: String,
    val userCategoryConfigId: String,
    val company: Company,
    val spentDate: String,
    val finishDate: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val spentMoney: Double,
    val oriSpentMoney: Double,
    val installmentCnt: Int,
    val keyword: String,
    val repeatType: Int = 0,
    val currency: String,
    val dwType: Int,
    val memo: String,
    val sms: SMS,
    val isDeleted: Boolean = false,
    val isOffset: Boolean = false,
    val isCustom: Boolean = false,
    val isUserUpdate: Boolean = false,
    val isUpdateAll: Boolean = false
) : Entity

data class Company(
    val id: String,
    val name: String,
    val address: String
)
