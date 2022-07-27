package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.parser.SMS
import com.tenqube.visualbase.domain.transaction.Company
import com.tenqube.visualbase.domain.transaction.Transaction

@Entity(tableName = "transaction")
data class TransactionModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "cardId")
    val cardId: String,
    @ColumnInfo(name = "userCategoryConfigId")
    val userCategoryConfigId: String,
    @Embedded
    val company: CompanyModel,
    @ColumnInfo(name = "spentDate")
    val spentDate: String,
    @ColumnInfo(name = "finishDate")
    val finishDate: String,
    @ColumnInfo(name = "lat")
    val lat: Double = 0.0,
    @ColumnInfo(name = "lng")
    val lng: Double = 0.0,
    @ColumnInfo(name = "spentMoney")
    val spentMoney: Double,
    @ColumnInfo(name = "oriSpentMoney")
    val oriSpentMoney: Double,
    @ColumnInfo(name = "installmentCnt")
    val installmentCnt: Int,
    @ColumnInfo(name = "keyword")
    val keyword: String,
    @ColumnInfo(name = "repeatType")
    val repeatType: Int = 0,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "dwType")
    val dwType: Int,
    @ColumnInfo(name = "memo")
    val memo: String,
    @Embedded
    val sms: SMSModel,
    @ColumnInfo(name = "regId")
    val regId: Int,
    @ColumnInfo(name = "classCode")
    val classCode: String,
    @ColumnInfo(name = "isDeleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "isOffset")
    val isOffset: Boolean = false,
    @ColumnInfo(name = "isCustom")
    val isCustom: Boolean = false,
    @ColumnInfo(name = "isUserUpdate")
    val isUserUpdate: Boolean = false,
    @ColumnInfo(name = "isUpdateAll")
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
    @ColumnInfo(name = "company_id")
    val id: String,
    @ColumnInfo(name = "companyName")
    val name: String,
    @ColumnInfo(name = "companyAddress")
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
    @ColumnInfo(name = "sms_id")
    val id: Int,
    @ColumnInfo(name = "fullSms")
    val fullSms: String,
    @ColumnInfo(name = "originTel")
    val originTel: String,
    @ColumnInfo(name = "displayTel")
    val displayTel: String,
    @ColumnInfo(name = "smsDate")
    val smsDate: String,
    @ColumnInfo(name = "smsType")
    val smsType: Int,
    @ColumnInfo(name = "title")
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
