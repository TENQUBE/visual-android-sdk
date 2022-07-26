package com.tenqube.ibk

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.tenqube.ibk.VisualFragment.Companion.VISUAL_IBK_ARG
import com.tenqube.shared.util.Utils
import com.tenqube.shared.util.decodeBase64
import com.tenqube.shared.util.inTransaction
import com.tenqube.visualbase.infrastructure.adapter.notification.VisualIBKReceiptDto
import com.tenqube.visualbase.infrastructure.framework.parser.rcs.RcsCatchReceiver
import com.tenqube.visualbase.infrastructure.framework.parser.sms.SMSCatchReceiver
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
                val url = it.decodeBase64()
                val receipt = Utils.fromJson(url, VisualIBKReceiptDto::class.java)
                replace(R.id.container, VisualFragment.newInstance(VisualIBKArg(url = receipt.toLink())))
            } ?: start(this)
        }
    }

    private fun start(fragmentTransaction: FragmentTransaction): FragmentTransaction {
        val arg = intent.getSerializableExtra(VISUAL_IBK_ARG) as VisualIBKArg
        fragmentTransaction.replace(R.id.container, VisualFragment.newInstance(arg))
        return fragmentTransaction
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupReceiver() {
        val smsReceiver = SMSCatchReceiver();
        val intentFilter = IntentFilter();
        intentFilter.addAction(SMSCatchReceiver.SMS_RECEIVED_ACTION);
        this.registerReceiver(smsReceiver,intentFilter);

        val rcsReceiver = RcsCatchReceiver();
        val intentFilter2 = IntentFilter();
        intentFilter2.addAction(RcsCatchReceiver.RCS_RECEIVED_ACTION);
        this.registerReceiver(rcsReceiver,intentFilter2);
    }
}
