package com.tenqube.visualbase.infrastructure.data.transaction.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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
            memo
        )
    }

    companion object {
        fun fromDomain(item: Transaction) : TransactionModel {
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
                item.memo
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
