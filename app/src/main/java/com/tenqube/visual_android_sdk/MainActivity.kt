package com.tenqube.visual_android_sdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.webui.UIServiceBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIServiceBuilder()
            .with(this)
            .build()
    }
}
