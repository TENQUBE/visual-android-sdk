package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.transaction.Company
import com.tenqube.visualbase.domain.transaction.Transaction

@Entity
data class TransactionModel(
    @PrimaryKey val id: String,
    val categoryId: String,
    val cardId: String,
    val userCategoryConfigId: String,
    @Embedded
    val company: CompanyModel,
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
    @Embedded
    val sms: SMSModel,
    val regId: Int,
    val classCode: String,
    val isDeleted: Boolean = false,
    val isOffset: Boolean = false,
    val isCustom: Boolean = false,
    val isUserUpdate: Boolean = false,
    val isUpdateAll: Boolean = false
) {
    fun asDomain(): Transaction {
        return Transaction(
            id,
            categoryId,
            cardId,
            userCategoryConfigId,
            company.asDomain(),
            spentDate,
            finishDate,
            lat,
            lng,
            spentMoney,
            oriSpentMoney,
            installmentCnt,
            keyword,
            repeatType,
            currency,
            dwType,
            memo,
            sms.asDomain(),
            regId,
            classCode
        )
    }

    companion object {
        fun fromDomain(item: Transaction): TransactionModel {
            return TransactionModel(
                item.id,
                item.categoryId,
                item.cardId,
                item.userCategoryConfigId,
                CompanyModel.fromDomain(item.company),
                item.spentDate,
                item.finishDate,
                item.lat,
                item.lng,
                item.spentMoney,
                item.oriSpentMoney,
                item.installmentCnt,
                item.keyword,
                item.repeatType,
                item.currency,
                item.dwType,
                item.memo,
                SMSModel.fromDomain(item.sms),
                item.regId,
                item.classCode
            )
        }
    }
}

data class CompanyModel(
    @ColumnInfo(name = "company_id") val id: String,
    val name: String,
    val address: String
) {
    fun asDomain(): Company {
        return Company(id, name, address)
    }

    companion object {
        fun fromDomain(item: Company): CompanyModel {
            return CompanyModel(item.id, item.name, item.address)
        }
    }
}

data class SMSModel(
    @ColumnInfo(name = "sms_id") val id: Int,
    val fullSms: String,
    val originTel: String,
    val displayTel: String,
    val smsDate: String,
    val smsType: Int,
    val title: String
) {
    fun asDomain(): SMS {
        return SMS(
            id,
            fullSms,
            originTel,
            displayTel,
            smsDate,
            smsType,
            title
        )
    }

    companion object {
        fun fromDomain(item: SMS): SMSModel {
            return SMSModel(
                item.smsId,
                item.fullSms,
                item.originTel,
                item.displayTel,
                item.smsDate,
                item.smsType,
                item.title
            )
        }
    }
}
