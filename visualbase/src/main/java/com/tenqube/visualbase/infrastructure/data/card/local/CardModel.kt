package com.tenqube.visualbase.infrastructure.data.card.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.card.Card

@Entity(tableName = "cards")
data class CardModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "subType") val subType: Int,
    @ColumnInfo(name = "displayName") val displayName: String,
    @ColumnInfo(name = "displayType") val displayType: Int,
    @ColumnInfo(name = "displaySubType") val displaySubType: Int,
    @ColumnInfo(name = "billingDay") val billingDay: Int,
    @ColumnInfo(name = "balance") val balance: Double,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "isExcept") val isExcept: Boolean,
    @ColumnInfo(name = "isCustom") val isCustom: Boolean,
    @ColumnInfo(name = "isDeleted") val isDeleted: Boolean
) {
    fun asDomain(): Card {
        return Card(
            id,
            userId,
            name,
            type,
            subType,
            displayName,
            displayType,
            displaySubType,
            billingDay,
            balance,
            memo,
            isExcept,
            isCustom,
            isDeleted
        )
    }

    companion object {
        fun fromDomain(card: Card): CardModel {
            return CardModel(
                card.id,
                card.userId,
                card.name,
                card.type,
                card.subType,
                card.displayName,
                card.displayType,
                card.displaySubType,
                card.billingDay,
                card.balance,
                card.memo,
                card.isExcept,
                card.isCustom,
                card.isDeleted
            )
        }
    }
}
