package com.tenqube.visual_android_sdk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tenqube.ibk.Layer
import com.tenqube.ibk.UserArg
import com.tenqube.ibk.VisualGender
import com.tenqube.ibk.VisualServiceBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val jbButton: Button = findViewById(R.id.jb)
        checkPermission()
        jbButton.setOnClickListener {
            VisualServiceBuilder()
                .activity(this)
                .apiKey("8rbfmPLFN12RmDH4Bq73n7y7o1UUeP3R1D4Oon2q")
                .layer(Layer.PROD)
                .build()
                .start(UserArg("uid", 1987, VisualGender.MALE))
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_SMS
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                    0
                )
            }
        }
    }
}
