package com.tenqube.visualbase.domain.transaction

import com.tenqube.visualbase.domain.Entity
import java.util.*

data class Transaction(
    override val id: String = UUID.randomUUID().toString(),
    val categoryId: String,
    val cardId: String,
    val userId: String,
    val userCategoryConfigId: String,
    val company: Company,
    val smsList: List<SMS>,
    val spentDate: String,
    val finishDate: String,
    val lat: Double,
    val lng: Double,
    val spentMoney: Double,
    val oriSpentMoney: Double,
    val installmentCnt: Int,
    val keyword: String,
    val searchKeyword: String,
    val repeatType: Int,
    val currency: String,
    val dwType: Int,
    val memo: String,
    val isDeleted: Boolean,
    val isOffset: Boolean,
    val isCustom: Boolean,
    val isUserUpdate: Boolean,
    val isUpdateAll: Boolean,
    val classCode: String,
    val isSynced: Boolean // 서버성공 플레그
    ) : Entity {
}

data class Company(
    val id: String,
    val name: String,
    val address: String
)

data class SMS(
    val smsId: String,
    val sender: String,
    val title: String,
    val fullSms: String,
    val smsDate: String,
    val smsType: Int,
    val identifier: String,
    val regId: Int
)
