package com.example.httprequest

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HttpRequestBuilder {

    private lateinit var context: Context
    private lateinit var config: HttpConfig
    private lateinit var transformer: ApiDataTransformer

    fun with(appContext: Context): HttpRequestBuilder {
        context = appContext
        return this
    }

    fun config(sourceFileId: Int): HttpRequestBuilder {
        config = HttpConfig.loadConfig(context, sourceFileId)
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

    fun build(): HttpRequest {
        val client = createOkHttpClient()
        if (!this::transformer.isInitialized) {
            transformer = ApiDataDefaultTransformer()
        }
        return HttpRequest(config, client, transformer)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.requestTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }
}