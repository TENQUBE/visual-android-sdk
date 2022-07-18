package com.tenqube.ibk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.shared.prefs.PrefStorage

class VisualServiceImpl(
    private val arg: VisualArg,
    prefStorage: PrefStorage
) : VisualService {

    init {
        prefStorage.apiKey = arg.apiKey
        prefStorage.layer = arg.layer.value
        prefStorage.service = "ibk"
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