package com.example.androidx_example.until.api

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.androidx_example.R
import com.example.androidx_example.until.tool.debugInfo
import java.util.*
import kotlin.collections.HashMap
import io.reactivex.Observable
import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import io.reactivex.schedulers.Schedulers
import okhttp3.EventListener
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * Request config param name
 */
const val REQUEST_HOST = "REQUEST_HOST"
const val WEB_SOCKET_HOST = "WEB_SOCKET_HOST"
const val REQUEST_TIMEOUT = "REQUEST_TIMEOUT"
const val RECONNECT_TIME = "RECONNECT_TIME"

/**
 * Request config default value
 */
const val REQUEST_DEFAULT_HOST = ""
const val WEB_SOCKET_DEFAULT_HOST = ""
const val REQUEST_DEFAULT_TIME_OUT = "10"
const val RECONNECT_TIME_DEFAULT_TIME = "2"

/**
 * Error  message string
 */
const val RESPONSE_DATA_ERROR = "数据格式错误"
const val RESPONSE_BODY_EMPTY = "响应体内容为空"
const val REQUEST_ERROR = "请求错误，服务器响应异常"

/**
 * Http Code
 */
const val CODE_200 = 200
const val CODE_401 = 401
const val CODE_403 = 403
const val CODE_422 = 422
const val CODE_500 = 500
const val CODE_SUCCESS = CODE_200
// const val CODE_ERROR = CODE_500
const val HTTP_CODE_UNKNOWN_MESSAGE = "其它错误"

val HTTP_CODE_MESSAGES = mapOf(
    CODE_200 to "请求成功",
    CODE_401 to "令牌错误",
    CODE_403 to "权限错误",
    CODE_422 to "参数错误",
    CODE_500 to "服务器错误"
)

val JsonElement.asOnlyString: String
    get() = when {
        isJsonArray || isJsonObject -> toString()
        else -> asString
    }

class HttpRequest {

    companion object {
        private var instance: OkHttpClient? = null
        private var config: HttpConfig? = null

        fun loadConfig(context: Context, rawId: Int = R.raw.http) {
            val inputStream = context.resources.openRawResource(rawId)
            val properties = Properties()
            properties.load(inputStream)
            config =
                HttpConfig(
                    requestHost = properties.getProperty(
                        REQUEST_HOST,
                        REQUEST_DEFAULT_HOST
                    ),
                    webSocketHost = properties.getProperty(
                        WEB_SOCKET_HOST,
                        WEB_SOCKET_DEFAULT_HOST
                    ),
                    requestTimeout = properties.getProperty(
                        REQUEST_TIMEOUT,
                        REQUEST_DEFAULT_TIME_OUT
                    ).toInt(),
                    reconnectTime = properties.getProperty(
                        RECONNECT_TIME,
                        RECONNECT_TIME_DEFAULT_TIME
                    ).toInt()
                )
        }

        fun get(apiName: String, params: HashMap<String, Any>? = null): Observable<ApiData> {
            val request = prepareGetRequest(apiName, params)
            return sendRequest(request)
        }

        private fun prepareGetRequest(
            apiName: String,
            params: HashMap<String, Any>? = null
        ): Request {
            var apiPath = apiName
            if (params != null) apiPath += getQueryString(
                params
            )
            apiPath = getRequestUrl(apiPath)
            return Request.Builder().url(apiPath).build()
        }

        fun getSync(apiName: String, params: HashMap<String, Any>? = null): ApiData {
            val request = prepareGetRequest(apiName, params)
            return sendSyncRequest(request)
        }

        fun post(apiName: String, params: HashMap<String, Any>? = null): Observable<ApiData> {
            val request =
                createPostRequest(apiName, params)
            return sendRequest(request)
        }

        fun download(downloadName: String): Response {
            val request = createDownloadRequest(downloadName)
            return getInstance().newCall(request)
                .execute()
        }

        fun webSocket(
            authToken: String,
            protocol: String,
            messageCallbackFun: (type: WebSocketContentType, content: String, webSocket: WebSocket) -> Unit
        ): WebSocket {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(config!!.requestTimeout.toLong(), TimeUnit.SECONDS)
                .pingInterval(config!!.requestTimeout.toLong(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
            val request = Request.Builder()
                .addHeader("Sec-WebSocket-Protocol", protocol)
                .url("${config!!.webSocketHost}\\token=$authToken")
                .build()
            val listener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) =
                    messageCallbackFun(
                        WebSocketContentType.OPEN_MESSAGE,
                        response.message(),
                        webSocket
                    )

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    webSocket.cancel()
                    Thread.sleep(config!!.reconnectTime * 1000L)
                    messageCallbackFun(
                        WebSocketContentType.ERROR_MESSAGE,
                        t.toString(),
                        okHttpClient.newWebSocket(request, this)
                    )
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) =
                    messageCallbackFun(WebSocketContentType.CLOSE_MESSAGE, reason, webSocket)

                override fun onMessage(webSocket: WebSocket, text: String) =
                    messageCallbackFun(WebSocketContentType.MESSAGE, text, webSocket)
            }

            return okHttpClient.newWebSocket(request, listener)
        }

        private fun getInstance(): OkHttpClient {
            // CacheControl.Builder().onlyIfCached().build()
            return instance ?: OkHttpClient.Builder()
                .connectTimeout(config!!.requestTimeout.toLong(), TimeUnit.SECONDS)
                .eventListener(RequestDebugListener())
                .addInterceptor(RequestInterceptor())
                .build()
        }

        private fun getRequestUrl(apiName: String): String {
            return "${config!!.requestHost}/$apiName"
        }

        private fun createPostRequest(
            requestUrl: String,
            params: HashMap<String, Any>? = null
        ): Request {
            var requestBuilder = Request.Builder()
            requestBuilder = requestBuilder.url(
                getRequestUrl(
                    requestUrl
                )
            )
            if (params != null) {
                val jsonStr = Gson().toJson(params)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr)
                requestBuilder.post(body)
            }
            return requestBuilder.build()
        }

        private fun createDownloadRequest(requestUrl: String): Request {
            var requestBuilder = Request.Builder()
            requestBuilder = requestBuilder.url(requestUrl)
            return requestBuilder.build()
        }

        private fun getQueryString(params: HashMap<String, Any>): String {
            val builder = Uri.parse("").buildUpon()
            params.keys.forEach {
                builder.appendQueryParameter(it, params[it].toString())
            }
            return builder.build().toString()
        }

        /**
         *  发送同步请求（阻塞当前线程）
         */
        private fun sendSyncRequest(request: Request): ApiData {
            return try {
                val response = getInstance().newCall(request).execute()
                val body = response.body()
                return when (val code = response.code()) {
                    CODE_SUCCESS -> if (body != null) ApiData.createFromString(body.string())
                    else ApiData.errorData(RESPONSE_BODY_EMPTY, RESPONSE_BODY_EMPTY)
                    else -> ApiData.errorData(
                        HTTP_CODE_MESSAGES[code]
                            ?: HTTP_CODE_UNKNOWN_MESSAGE,
                        body?.string() ?: RESPONSE_BODY_EMPTY
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ApiData.errorData(REQUEST_ERROR, REQUEST_ERROR)
            }
        }

        /**
         *  发送异步请求，返回观察对象
         */
        private fun sendRequest(request: Request): Observable<ApiData> {
            return Observable
                .fromCallable<ApiData> {
                    return@fromCallable sendSyncRequest(request)
                }.subscribeOn(Schedulers.newThread())
                .onErrorReturn {
                    it.printStackTrace()
                    ApiData.errorData(REQUEST_ERROR, REQUEST_ERROR)
                }
        }
    }

    enum class WebSocketContentType {
        MESSAGE,
        OPEN_MESSAGE,
        CLOSE_MESSAGE,
        ERROR_MESSAGE
    }

    data class HttpConfig(
        val requestHost: String,
        var webSocketHost: String,
        val requestTimeout: Int,
        val reconnectTime: Int
    )

    data class ApiSourceData(
        val result: Boolean,
        val message: String,
        val data: String? = null
    )

    data class PageData<T>(
        var total: Int = 0,
        var rows: List<T> = listOf()
    )

    class ApiData(private val sourceData: ApiSourceData, originBodyStr: String) {

        init {
            debugInfo("响应内容", originBodyStr)
        }

        fun getMessage(): String {
            return sourceData.message
        }

        fun isOk(): Boolean {
            return sourceData.result
        }

        fun getStringData(): String {
            return sourceData.data ?: String()
        }

        fun <T> getObjectData(classOfT: Class<T>): T? {
            return try {
                Gson().fromJson(sourceData.data, classOfT)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun <T> getObjectList(classOfT: Class<T>): List<T> {
            var objectRows = listOf<T>()
            try {
                if (sourceData.data != null) {
                    val jsonArray = JsonParser().parse(sourceData.data).asJsonArray
                    objectRows = jsonArray.map {
                        Gson().fromJson(it.toString(), classOfT)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return objectRows
        }

        fun <T> getPageData(classOfT: Class<T>): PageData<T> {
            var pageData = PageData<T>()
            try {
                if (sourceData.data != null) {
                    val jsonObject = JsonParser().parse(sourceData.data).asJsonObject
                    val jsonArray = jsonObject.get("rows").asJsonArray
                    pageData = PageData(
                        total = jsonObject.get("total").asInt,
                        rows = jsonArray.map {
                            Gson().fromJson(it.toString(), classOfT)
                        })
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return pageData
        }

        companion object {

            fun errorData(errorMsg: String, responseData: String): ApiData {
                return ApiData(
                    ApiSourceData(result = false, message = errorMsg),
                    responseData
                )
            }

            fun successData(msg: String, data: String, responseData: String): ApiData {
                return ApiData(
                    ApiSourceData(
                        result = true,
                        message = msg,
                        data = data
                    ),
                    responseData
                )
            }

            fun createFromString(responseData: String): ApiData {

                val jsonObject = JsonParser().parse(responseData).asJsonObject
                return try {
                    ApiData(
                        ApiSourceData(
                            result = jsonObject.get("result").asBoolean,
                            message = jsonObject.get("message").asOnlyString,
                            data = jsonObject.get("data")?.asOnlyString
                        ),
                        responseData
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    errorData(RESPONSE_DATA_ERROR, responseData)
                }
            }
        }
    }

    class RequestDebugListener : EventListener() {

        override fun dnsStart(call: Call, domainName: String) {
            debugInfo("DNS解析开始")
        }

        override fun dnsEnd(
            call: Call,
            domainName: String,
            inetAddressList: MutableList<InetAddress>
        ) {
            debugInfo("DNS解析结束")
        }

        override fun connectFailed(
            call: Call,
            inetSocketAddress: InetSocketAddress,
            proxy: Proxy,
            protocol: Protocol?,
            ioe: IOException
        ) {
            super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
            inetSocketAddress.hostString
            debugInfo(
                """
                =====连接失败=====
                hostString:${inetSocketAddress.hostString}
                protocol:${protocol?.name}
                proxy:${proxy.type().name}
            """.trimIndent()
            )
        }

        override fun callStart(call: Call) {
//            super.callStart(call)
//            debugInfo(
//                """
//                =====开始运行=====
//
//            """.trimIndent()
//            )
        }

        override fun callFailed(call: Call, ioe: IOException) {
//            super.callFailed(call, ioe)
//            debugInfo(
//                """
//                =====执行失败=====
//
//            """.trimIndent()
//            )
        }
    }

    class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .url(originalRequest.url())
                .build()
            val response = chain.proceed(compressedRequest)
            Log.d(HttpRequest::class.java.name, "REQUEST_URL:${originalRequest.url()}")
            Log.d(HttpRequest::class.java.name, "RESPONSE_CODE:${response.code()}")
            return response
        }
    }
}