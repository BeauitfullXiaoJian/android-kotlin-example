package com.example.httprequest

/**
 * 响应的原始数据类
 */
data class ApiOriginData(
    val apiData: ApiData, // 处理好的接口数据对象
    val originBodyStr: String, // 原始响应体
    val code: Int // 响应状态码
) {
    companion object {

        /**
         * 创建一个失败ApiOriginData对象
         */
        fun error(
            errorMsg: String,
            responseData: String,
            body: String,
            code: Int
        ): ApiOriginData {
            return ApiOriginData(
                apiData = ApiData(
                    result = false,
                    message = errorMsg,
                    data = responseData
                ),
                originBodyStr = body,
                code = code
            )
        }

    }
}