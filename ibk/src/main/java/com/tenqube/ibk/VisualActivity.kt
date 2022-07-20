package com.tenqube.ibk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.ibk.VisualFragment.Companion.VISUAL_IBK_ARG
import com.tenqube.shared.util.inTransaction
import timber.log.Timber

class VisualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_ibk)
        Timber.DebugTree()
        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                val arg = intent.getSerializableExtra(VISUAL_IBK_ARG) as VisualIBKArg
                replace(R.id.container, VisualFragment.newInstance(arg))
            }
        }
    }
}
