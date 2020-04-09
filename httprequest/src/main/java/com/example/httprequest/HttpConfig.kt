package com.example.httprequest

import android.content.Context
import java.util.*

/**
 * Http配置类
 */
data class HttpConfig(
    val requestHost: String,
    val requestTimeout: Int,
    var webSocketHost: String,
    val reconnectTime: Int,
    val downloadByteNum: Int
) {
    companion object {

        /**
         * Request config param name
         */
        private const val REQUEST_HOST = "REQUEST_HOST"
        private const val WEB_SOCKET_HOST = "WEB_SOCKET_HOST"
        private const val REQUEST_TIMEOUT = "REQUEST_TIMEOUT"
        private const val RECONNECT_TIME = "RECONNECT_TIME"
        private const val DOWNLOAD_BYTE_NUM = "DOWNLOAD_BYTE_NUM"

        /**
         * Request config default value
         */
        private const val REQUEST_DEFAULT_HOST = ""
        private const val WEB_SOCKET_DEFAULT_HOST = ""
        private const val REQUEST_DEFAULT_TIME_OUT = "10"
        private const val RECONNECT_TIME_DEFAULT_TIME = "2"
        private const val DOWNLOAD_BYTE_DEFAULT_NUM = "1024"


        fun loadConfig(context: Context, rawId: Int): HttpConfig {
            val inputStream = context.resources.openRawResource(rawId)
            val properties = Properties()
            properties.load(inputStream)
            return HttpConfig(
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
                ).toInt(),
                downloadByteNum = properties.getProperty(
                    DOWNLOAD_BYTE_NUM,
                    DOWNLOAD_BYTE_DEFAULT_NUM
                ).toInt()
            )
        }
    }
}