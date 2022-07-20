package com.tenqube.visualbase.infrastructure.data.usercategoryconfig.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.usercategoryconfig.UserCategoryConfig

@Entity
data class UserCategoryConfigModel(
    @PrimaryKey val id: String,
    val userId: String,
    val code: String,
    val type: String,
    val isExcept: Boolean,
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