package com.tenqube.ibk

import androidx.appcompat.app.AppCompatActivity
import com.tenqube.shared.prefs.SharedPreferenceStorage

class VisualServiceBuilder {
    private lateinit var activity: AppCompatActivity
    private lateinit var apiKey: String
    private lateinit var layer: Layer

    fun activity(activity: AppCompatActivity): VisualServiceBuilder = apply {
        this.activity = activity
    }

    fun apiKey(apiKey: String) = apply {
        this.apiKey = apiKey
    }

    fun layer(layer: Layer) = apply {
        this.layer = layer
    }

    fun build(): VisualService {
        val prefStorage = SharedPreferenceStorage(activity)
        return VisualServiceImpl(
            VisualArg(
                activity,
                apiKey,
                layer
            ),
            prefStorage
        )
    }
}

enum class Layer(val value: String) {
    DEV("dev"),
    PROD("prod")
}