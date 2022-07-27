package com.tenqube.visualbase.infrastructure.data.user.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tenqube.visualbase.domain.user.User
import com.tenqube.visualbase.domain.user.UserConfig

@Entity
data class UserModel(
    @PrimaryKey val id: String,
    val birth: Int,
    val gender: Int,
    val gaId: String,
    @Embedded
    val userConfig: UserConfig
) {

    fun asDomain(): User {
        return User(
            id,
            birth,
            gender,
            gaId,
            userConfig
        )
    }
    companion object {
        fun fromDomain(item: User): UserModel {
            return UserModel(
                item.id,
                item.birth,
                item.gender,
                item.gaId,
                item.userConfig
            )
        }
    }
}
