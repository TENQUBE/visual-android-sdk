package com.tenqube.ibkreceipt

import androidx.appcompat.app.AppCompatActivity
import com.tenqube.shared.prefs.SharedPreferenceStorage
import java.io.Serializable

class VisualServiceBuilder {
    private lateinit var activity: AppCompatActivity
    private lateinit var apiKey: String
    private lateinit var layer: Layer
    private lateinit var service: Service
    private lateinit var notification: NotificationArg

    fun activity(activity: AppCompatActivity): VisualServiceBuilder = apply {
        this.activity = activity
    }

    fun service(service: Service) = apply {
        this.service = service
    }

    fun apiKey(apiKey: String) = apply {
        this.apiKey = apiKey
    }

    fun layer(layer: Layer) = apply {
        this.layer = layer
    }

    fun notification(arg: NotificationArg) = apply {
        this.notification = arg
    }

    fun build(): VisualService {
        val prefStorage = SharedPreferenceStorage(activity)
        return VisualServiceImpl(
            VisualArg(
                service,
                activity,
                apiKey,
                layer,
                notification
            ),
            prefStorage
        )
    }
}

enum class Service(val value: String, val deepLink: String) {
    IBK("ibk", "visual://ibk-receipt-popup")
}

enum class Layer(val value: String) {
    DEV("dev"),
    PROD("prod")
}

data class NotificationArg(
    val smallIcon: Int,
    val channelId: String,
    val color: Int
) : Serializable
