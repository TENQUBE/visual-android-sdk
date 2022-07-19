package com.tenqube.visualbase.infrastructure.data.card.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "sub_type") val subType: Int,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "display_type") val displayType: Int,
    @ColumnInfo(name = "display_sub_type") val displaySubType: Int,
    @ColumnInfo(name = "billing_day") val billingDay: Int,
    @ColumnInfo(name = "balance") val balance: Double,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "is_except") val isExcept: Boolean,
    @ColumnInfo(name = "is_custom") val isCustom: Boolean,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
