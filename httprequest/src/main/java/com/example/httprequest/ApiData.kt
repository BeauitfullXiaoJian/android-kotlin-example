package com.example.httprequest

import com.example.httprequest.HttpRequest.Companion.RESPONSE_DATA_ERROR
import com.google.gson.Gson
import com.google.gson.JsonParser

class ApiData(
    val result: Boolean, // 接口成功|失败
    val message: String, // 接口提示消息
    val data: String?,  // 接口数据,强制为String类型
    val error: String = ""  // 错误信息
) {

    private var exception: Exception? = null

    fun storeException(e: Exception): ApiData {
        exception = e
        return this;
    }

    fun printException(): ApiData {
        exception?.printStackTrace()
        return this
    }

    /**
     * 判断请求是否成功
     */
    fun isOk(): Boolean {
        return result
    }

    /**
     * 获取响应结果原始字符串
     */
    fun getStringData(): String {
        return data ?: String()
    }

    /**
     * 获取响应结果数据对象
     * @param
     */
    fun <T> getObjectData(classOfT: Class<T>): T? {
        return try {
            Gson().fromJson(data, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取响应结果数据对象
     */
    fun <T> getObjectData(classOfT: Class<T>, defaultValue: T): T {
        return try {
            Gson().fromJson(data, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * 获取响应结果数据对象，带默认值
     */
    fun <T> getObjectList(classOfT: Class<T>): List<T> {
        var objectRows = listOf<T>()
        try {
            if (data != null) {
                val jsonArray = JsonParser().parse(data).asJsonArray
                objectRows = jsonArray.map {
                    Gson().fromJson(it.toString(), classOfT)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return objectRows
    }

    /**
     * 将响应结果转换为分页对象
     */
    fun <T> getPageData(classOfT: Class<T>): Pagination.PageData<T> {
        return data?.let { Pagination.stringToPageData(it, classOfT) } ?: Pagination.PageData()
    }

    companion object {

        fun dataError(data: String, error: String): ApiData = ApiData(
            result = false,
            message = RESPONSE_DATA_ERROR,
            data = data,
            error = error
        )
    }
}
