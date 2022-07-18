package com.tenqube.ibk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.shared.prefs.PrefStorage

class VisualServiceImpl(
    private val arg: VisualArg,
    prefStorage: PrefStorage
) : VisualService {

    init {
        prefStorage.saveApiKey(arg.apiKey)
        prefStorage.saveLayer(arg.layer.name)
    }

    override fun start() {
        val intent = Intent(arg.activity, VisualActivity::class.java)
        arg.activity.startActivity(intent)
    }
}

data class VisualArg(
    val activity: AppCompatActivity,
    val apiKey: String,
    val layer: Layer
)