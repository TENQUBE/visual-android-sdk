package com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig

@Entity(tableName = "userCategoryConfig")
data class UserCategoryConfigModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "isExcept")
    val isExcept: Boolean,
    @ColumnInfo(name = "isMain")
    val isMain: Boolean
) {
    fun asDomain(): UserCategoryConfig {
        return UserCategoryConfig(
            id,
            userId,
            code,
            type,
            isExcept,
            isMain
        )
    }

    companion object {
        fun fromDomain(item: UserCategoryConfig): UserCategoryConfigModel {
            return UserCategoryConfigModel(
                item.id,
                item.userId,
                item.code,
                item.type,
                item.isExcept,
                item.isMain
            )
        }
    }
}
