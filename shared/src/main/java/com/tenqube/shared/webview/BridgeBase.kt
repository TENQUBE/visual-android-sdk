package com.tenqube.shared.webview

import android.util.Log
import android.webkit.WebView
import com.tenqube.shared.error.ParameterError
import com.tenqube.shared.util.elapsedLog
import com.tenqube.shared.util.fromJson
import com.tenqube.shared.util.toJson
import com.tenqube.shared.webview.dto.RequestBody
import com.tenqube.shared.webview.dto.StatusCode
import com.tenqube.shared.webview.dto.WebLog
import com.tenqube.shared.webview.dto.WebResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BridgeBase(
    private val webView: WebView
) : CoroutineScope {

    var job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private fun getJs(callback: String, data: String): String {
        var dataStr = data
        if (dataStr.isNotEmpty()) dataStr = "'$dataStr'"
        return "$JS$callback($dataStr);"
    }

    open suspend fun onSuccess(funcName: String, any: Any? = null): WebResponse<*> =
        withContext(Dispatchers.Main) {
            val response = WebResponse(
                statusCode = StatusCode.Success.code,
                msg = StatusCode.Success.name, body = any
            )
            val url: String = getJs(
                makeResponseCallback(funcName),
                response.toJson()
            )
            webView.loadUrl(url)
            return@withContext response
        }

    private fun makeResponseCallback(callback: String): String {
        return RESPONSE + callback
    }

    open suspend fun onResultError(funcName: String, msg: String) =
        withContext(Dispatchers.Main) {
            val response = WebResponse<Any>(
                statusCode = StatusCode.ServerError.code,
                msg = msg
            )
            val url: String = getJs(
                makeResponseCallback(funcName),
                response.toJson()
            )

            webView.loadUrl(url)
        }

    open fun <T> parseRequest(params: String, classOfT: Class<T>): RequestBody {

        return params.fromJson(classOfT)?.run {
            if (this == null || this !is RequestBody) {
                throw ParameterError(StatusCode.JSONParsingError.name)
            } else {
                this
            }
        } as RequestBody
    }

    fun <T> execute(funcName: String, params: String? = null, classOfT: Class<T>, body: (T?) -> Any?) {
        launch(Dispatchers.Main) {
            var request: RequestBody? = null
            var response: Any? = null
            val startTime = System.currentTimeMillis()
            try {
                if (params != null) {
                    request = parseRequest(params, classOfT)
                    request.checkParams()
                }
                response = onSuccess(
                    funcName = funcName,
                    any = body(request as? T?)
                )
            } catch (e: Exception) {
                onResultError(funcName, e.toString())
            } finally {
                startTime.elapsedLog(
                    this.javaClass,
                    WebLog(
                        funcName = funcName,
                        request = request,
                        response = response
                    ).toJson()
                )
            }
        }
    }

    companion object {
        const val ERROR_CALLBACK = "onError"
        const val ON_FINISH = "onFinish"
        const val RESPONSE = "response."
        const val JS = "javascript:window."
    }
}
