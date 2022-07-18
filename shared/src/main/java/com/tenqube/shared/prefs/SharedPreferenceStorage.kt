package com.tenqube.shared.prefs

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceStorage constructor(context: Context
) : PrefStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override var accessToken by StringPreference(
        prefs,
        PREF_ACCESS_TOKEN,
        ""
    )

    override var layer by StringPreference(
        prefs,
        PREF_LAYER,
        ""
    )

    override var apiKey by StringPreference(
        prefs,
        PREF_API_KEY,
        ""
    )

    override var searchUrl by StringPreference(
        prefs,
        PREF_SEARCH_URL,
        ""
    )

    override var searchApiKey by StringPreference(
        prefs,
        PREF_SEARCH_API_KEY,
        ""
    )

    override var resourceUrl by StringPreference(
        prefs,
        PREF_RESOURCE_URL,
        ""
    )

    override var resourceApiKey by StringPreference(
        prefs,
        PREF_RESOURCE_API_KEY,
        ""
    )

    override var ruleVersion by IntPreference(
        prefs,
        PREF_RULE_VERSION,
        0
    )

    override var syncCnt by IntPreference(
        prefs,
        PREF_SYNC_CNT,
        0
    )

    override var service by StringPreference(
        prefs,
        PREF_SERVICE,
        ""
    )

    override var lastRcsTime by LongPreference(
        prefs,
        PREF_LAST_RCS_TIME,
        0
    )

    companion object {
        const val PREFS_NAME = "visual"
        const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        const val PREF_LAYER = "PREF_LAYER"
        const val PREF_API_KEY = "PREF_API_KEY"
        const val PREF_SEARCH_URL = "PREF_SEARCH_URL"
        const val PREF_SEARCH_API_KEY = "PREF_SEARCH_API_KEY"
        const val PREF_RESOURCE_URL = "PREF_RESOURCE_URL"
        const val PREF_RESOURCE_API_KEY = "PREF_RESOURCE_API_KEY"
        const val PREF_RULE_VERSION = "PREF_RULE_VERSION"
        const val PREF_SYNC_CNT = "PREF_SYNC_CNT"
        const val PREF_SERVICE = "PREF_SERVICE"
        const val PREF_LAST_RCS_TIME = "PREF_LAST_RCS_TIME"
    }
}