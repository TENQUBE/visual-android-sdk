package com.tenqube.visual_android_sdk

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.ibk.Layer
import com.tenqube.ibk.UserArg
import com.tenqube.ibk.VisualGender
import com.tenqube.ibk.VisualServiceBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val jbButton: Button = findViewById(R.id.jb)
        jbButton.setOnClickListener {
            VisualServiceBuilder()
                .activity(this)
                .apiKey("r8scLJTRdd8NgE1EEVkaU1hoyQDRr6G76kIskuyr")
                .layer(Layer.PROD)
                .build()
                .start(UserArg("uid", 1987, VisualGender.MALE))
        }
    }
}
