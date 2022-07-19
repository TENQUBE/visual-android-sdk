package com.tenqube.visualbase.infrastructure.data.user.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.user.UserConfig

@Entity
data class UserModel(
    @PrimaryKey val id: String,
    val birth: Int,
    val gender: Int,
    val gaId: String,
    @Embedded
    val userConfig: UserConfig
)