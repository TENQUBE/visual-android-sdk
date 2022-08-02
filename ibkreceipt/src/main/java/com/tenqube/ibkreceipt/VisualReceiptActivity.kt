package com.tenqube.ibkreceipt

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.tenqube.ibkreceipt.VisualFragment.Companion.VISUAL_IBK_ARG
import com.tenqube.shared.util.inTransaction
import com.tenqube.visualbase.infrastructure.adapter.notification.VisualIBKReceiptDto
import timber.log.Timber

class VisualReceiptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual_ibk)
        Timber.DebugTree()
        findViewById<ConstraintLayout>(R.id.container).setBackgroundColor(Color.TRANSPARENT)
        if (savedInstanceState == null) {
            start()
        }
    }

    private fun start() {
        supportFragmentManager.inTransaction {
            try {
                intent.data?.let {
                    val receipt = VisualIBKReceiptDto.from(it)
                    replace(R.id.container, VisualFragment.newInstance(VisualIBKArg(url = receipt.toLink())))
                } ?: start(this)
            } catch (e: Exception) {
                start(this)
            }
        }
    }

    private fun start(fragmentTransaction: FragmentTransaction): FragmentTransaction {
        val arg = (intent.getSerializableExtra(VISUAL_IBK_ARG) as VisualIBKArg?) ?: VisualIBKArg(null, null)
        fragmentTransaction.replace(R.id.container, VisualFragment.newInstance(arg))
        return fragmentTransaction
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
