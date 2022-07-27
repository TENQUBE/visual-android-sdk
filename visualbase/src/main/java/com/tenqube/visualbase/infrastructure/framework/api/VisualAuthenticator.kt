package com.tenqube.visualbase.infrastructure.framework.api

import com.tenqube.shared.prefs.PrefStorage
import com.tenqube.visualbase.domain.auth.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class VisualAuthenticator(private val authService: AuthService, private val prefStorage: PrefStorage) : Authenticator {
    private fun getRequest(response: Response): Request {
        return response.request().newBuilder()
            .header("Authorization", prefStorage.accessToken)
            .build()
    }

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        try {
            val token = authService.reissue(prefStorage.refreshToken)
            prefStorage.accessToken = token.accessToken
            prefStorage.refreshToken = token.refreshToken
        } catch (e: Exception) {
            throw IOException(e.toString())
        }
        return@runBlocking getRequest(response)
    }
}
