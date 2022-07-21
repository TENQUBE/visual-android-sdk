package com.tenqube.visualbase.infrastructure.framework.api

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visual_third.domain.pref.PrefStorage
import com.tenqube.visual_third.domain.util.getValue
import com.tenqube.visualbase.domain.auth.AuthService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.io.IOException

class VisualAuthenticator(private val authService: AuthService, private val prefStorage: PrefStorage) : Authenticator {
    private fun getRequest(response: Response): Request {
        return response.request.newBuilder()
                .header("Authorization", prefStorage.accessToken)
                .build()
    }

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            val token = authService.reissue(prefStorage.re()).getValue()
            prefStorage.accessToken = token
        } catch (e: Exception) {
            throw IOException(e.toString())
        }
        Timber.i("VISUAL  result")

        return getRequest(response)
    }
}