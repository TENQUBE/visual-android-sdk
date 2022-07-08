package com.tenqube.visual_android_sdk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.jb.VisualActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jbButton: Button = findViewById(R.id.jb)
        val ibkButton: Button = findViewById(R.id.ibk)

        jbButton.setOnClickListener {
            val intent = Intent(this, VisualActivity::class.java)
            startActivity(intent)
        }

        ibkButton.setOnClickListener {
            val intent = Intent(this, com.tenqube.ibk.VisualActivity::class.java)
            startActivity(intent)
        }
    }
}
