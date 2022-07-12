package com.tenqube.visualbase.domain.user

import com.tenqube.visualbase.domain.Entity
import com.tenqube.visualbase.domain.user.command.CreateUser

data class User(
    override val id: String = "anonymous",
    val birth: Int = 0,
    val gender: Int = 0,
    val gaId: String = "",
    val fcm: String? = null,
    val userConfig: UserConfig = UserConfig(userId = id)
) : Entity {
    companion object {
        fun from(createUser: CreateUser): User {
            return User(
                id = createUser.uid,
                gender = createUser.gender
            )
        }
    }
}

data class UserConfig(
    val userId: String,
    val startDay: Int = 1,
    val isActivePopup: Boolean = false,
    val isActiveWeekly: Boolean = true,
    val isActiveDaily: Boolean = false,
    val isActiveMonthly: Boolean = true,
    val isActiveNotiCatch: Boolean = false
)
