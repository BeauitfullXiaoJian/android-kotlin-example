package com.example.httprequest

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HttpRequestBuilder {

    private lateinit var context: Context
    private lateinit var config: HttpConfig
    private lateinit var transformer: ApiDataTransformer
    private lateinit var dataSaver: ApiDataSaver

    fun with(appContext: Context): HttpRequestBuilder {
        context = appContext
        return this
    }

    fun config(sourceFileId: Int): HttpRequestBuilder {
        config = HttpConfig.loadConfig(context, sourceFileId)
        HttpRequest.log("请求地址")
        HttpRequest.log(config.requestHost)
        return this
    }

    fun config(customConfig: HttpConfig): HttpRequestBuilder {
        config = customConfig.copy()
        return this
    }

    fun dataTransformer(apiOriginDataTransformer: ApiDataTransformer): HttpRequestBuilder {
        transformer = apiOriginDataTransformer
        return this
    }

    fun apiDataSaver(apiDataSaver: ApiDataSaver): HttpRequestBuilder {
        dataSaver = apiDataSaver
        return this
    }

    fun build(): HttpRequest {
        val client = createOkHttpClient()
        if (!this::transformer.isInitialized) {
            transformer = ApiDataDefaultTransformer()
        }
        if (!this::dataSaver.isInitialized) {
            dataSaver = object : ApiDataSaver {
                override fun save(apiName: String, paramsHash: Int, saveData: String) {}
                override fun find(apiName: String, paramsHash: Int): String? = null
            }
        }
        return HttpRequest(config, client, transformer, dataSaver)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.requestTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }
}