package com.tenqube.jb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tenqube.reward.util.inTransaction
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.DebugTree()
        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                replace(R.id.container, MainFragment.newInstance())
            }
        }
    }
}