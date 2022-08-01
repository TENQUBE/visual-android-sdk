package com.tenqube.ibkreceipt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.ibkreceipt.VisualFragment.Companion.VISUAL_IBK_ARG
import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.user.command.CreateUser

class VisualServiceImpl(
    private val arg: VisualArg,
    private val prefStorage: PrefStorage
) : VisualService {

    init {
        saveSDKConfig(arg)
    }

    private fun saveSDKConfig(arg: VisualArg) {
        prefStorage.apiKey = arg.apiKey
        prefStorage.layer = arg.layer.value
        prefStorage.service = arg.service.value
        prefStorage.visualReceiptDeepLink = arg.service.deepLink
        prefStorage.visualReceiptPopupDeepLink = arg.service.popup
        prefStorage.notiChannelId = arg.notification.channelId
        prefStorage.notiIcon = arg.notification.smallIcon
        prefStorage.notiColor = arg.notification.color
    }

    override fun start(command: UserArg) {
        val intent = Intent(arg.activity, VisualActivity::class.java)
        intent.putExtra(
            VISUAL_IBK_ARG,
            VisualIBKArg(
                user = CreateUser(
                    uid = command.uid,
                    birth = command.birth,
                    gender = command.gender.ordinal
                )
            )
        )
        arg.activity.startActivity(intent)
    }
}

data class VisualArg(
    val service: Service,
    val activity: AppCompatActivity,
    val apiKey: String,
    val layer: Layer,
    val notification: NotificationArg
)
