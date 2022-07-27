package com.tenqube.ibkreceipt

import android.content.Context
import android.preference.PreferenceManager

class IBKSharedPreference(private val context: Context) {

    fun disableTranPopup() {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit().putBoolean("TENQUBE_TRAN_POPUP", false).apply()
    }
}
