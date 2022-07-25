package com.tenqube.ibk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.tenqube.ibk.VisualFragment.Companion.VISUAL_IBK_ARG
import com.tenqube.shared.util.inTransaction
import timber.log.Timber

class VisualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_ibk)
        Timber.DebugTree()
        if (savedInstanceState == null) {
            start()
        }
    }

    private fun start() {
        supportFragmentManager.inTransaction {
            intent.data?.getQueryParameter("link")?.let {
                replace(R.id.container, VisualFragment.newInstance(VisualIBKArg(url = it)))
            } ?: start(this)
        }
    }

    private fun start(fragmentTransaction: FragmentTransaction): FragmentTransaction {
        val arg = intent.getSerializableExtra(VISUAL_IBK_ARG) as VisualIBKArg
        fragmentTransaction.replace(R.id.container, VisualFragment.newInstance(arg))
        return fragmentTransaction
    }
}
