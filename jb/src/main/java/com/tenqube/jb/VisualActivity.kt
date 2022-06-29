package com.tenqube.jb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.shared.util.inTransaction
import timber.log.Timber

class VisualActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_jb)
        Timber.DebugTree()
        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                replace(R.id.container, VisualFragment.newInstance())
            }
        }
    }
}
