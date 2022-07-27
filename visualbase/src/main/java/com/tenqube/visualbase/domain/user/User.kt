package com.tenqube.visualbase.domain.user

import androidx.room.ColumnInfo
import com.tenqube.visualbase.domain.Entity
import com.tenqube.visualbase.domain.user.command.CreateUser

data class User(
    override val id: String = "anonymous",
    val birth: Int = 0,
    val gender: Int = 0,
    val gaId: String = "",
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
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "startDay")
    val startDay: Int = 1,
    @ColumnInfo(name = "isActivePopup")
    val isActivePopup: Boolean = false,
    @ColumnInfo(name = "isActiveWeekly")
    val isActiveWeekly: Boolean = true,
    @ColumnInfo(name = "isActiveDaily")
    val isActiveDaily: Boolean = false,
    @ColumnInfo(name = "isActiveMonthly")
    val isActiveMonthly: Boolean = true,
    @ColumnInfo(name = "isActiveNotiCatch")
    val isActiveNotiCatch: Boolean = false
)
