package com.example.androidx_example.until.api

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.androidx_example.R
import com.example.androidx_example.until.debugInfo
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

/**
 * Request config param name
 */
const val REQUEST_HOST = "REQUEST_HOST"
const val REQUEST_TIMEOUT = "REQUEST_TIMEOUT"

/**
 * Request config default value
 */
const val REQUEST_DEFAULT_HOST = ""
const val REQUEST_DEFAULT_TIME_OUT = "5000"

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
    get() = if (isJsonObject) toString() else asString

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
                    requestTimeout = properties.getProperty(
                        REQUEST_TIMEOUT,
                        REQUEST_DEFAULT_TIME_OUT
                    ).toInt()
                )
        }

        fun get(apiName: String, params: HashMap<String, Any>? = null): Observable<ApiData> {
            var apiPath = apiName
            if (params != null) apiPath += getQueryString(
                params
            )
            apiPath = getRequestUrl(apiPath)
            val request = Request.Builder().url(apiPath).build()
            return sendRequest(request)
        }

        fun post(apiName: String, params: HashMap<String, Any>? = null): Observable<ApiData> {
            val request =
                createPostRequest(apiName, params)
            return sendRequest(request)
        }

        private fun getInstance(): OkHttpClient {
            // CacheControl.Builder().onlyIfCached().build()
            return instance ?: OkHttpClient.Builder()
                .eventListener(RequestDebugListener())
                .addInterceptor(RequestInterceptor())
                .build()
        }

        private fun getRequestUrl(apiName: String): String {
            return "${config!!.requestHost}/$apiName"
        }

        private fun createPostRequest(requestUrl: String, params: HashMap<String, Any>? = null): Request {
            var requestBuilder = Request.Builder()
            requestBuilder = requestBuilder.url(
                getRequestUrl(
                    requestUrl
                )
            )
            if (params != null) {
                val jsonStr = Gson().toJson(params)
                val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr)
                requestBuilder.post(body)
            }
            return requestBuilder.build()
        }

        private fun getQueryString(params: HashMap<String, Any>): String {
            val builder = Uri.parse("").buildUpon()
            params.keys.forEach {
                builder.appendQueryParameter(it, params[it].toString())
            }
            return builder.build().toString()
        }

        private fun sendRequest(request: Request): Observable<ApiData> {
            return Observable
                .fromCallable<ApiData> {
                    val response = getInstance().newCall(request).execute()
                    val body = response.body()
                    return@fromCallable when (val code = response.code()) {
                        CODE_SUCCESS -> if (body != null) ApiData.createFromString(
                            body.string()
                        )
                        else ApiData.errorData(RESPONSE_BODY_EMPTY, RESPONSE_BODY_EMPTY)
                        else -> ApiData.errorData(
                            HTTP_CODE_MESSAGES[code]
                                ?: HTTP_CODE_UNKNOWN_MESSAGE,
                            body?.string() ?: RESPONSE_BODY_EMPTY
                        )
                    }
                }.subscribeOn(Schedulers.newThread())
                .onErrorReturn {
                    it.printStackTrace()
                    ApiData.errorData(REQUEST_ERROR, REQUEST_ERROR)
                }
        }
    }

    data class HttpConfig(
        val requestHost: String,
        val requestTimeout: Int
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

    class ApiData(private val sourceData: ApiSourceData, val originBodyStr: String) {

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
            super.dnsStart(call, domainName)
            debugInfo("DNS解析开始")
        }

        override fun dnsEnd(call: Call, domainName: String, inetAddressList: MutableList<InetAddress>) {
            super.dnsEnd(call, domainName, inetAddressList)
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
            super.callStart(call)
            debugInfo(
                """
                =====开始运行=====

            """.trimIndent()
            )
        }

        override fun callFailed(call: Call, ioe: IOException) {
            super.callFailed(call, ioe)
            debugInfo(
                """
                =====执行失败=====

            """.trimIndent()
            )
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