package com.example.httprequest

import com.google.gson.Gson
import com.google.gson.JsonParser

class ApiData(
    val result: Boolean, // 接口调用结果，成功|失败
    val message: String, // 接口提示消息
    val data: String?   // 接口数据串
) {

    fun isOk(): Boolean {
        return result
    }

    fun getStringData(): String {
        return data ?: String()
    }

    fun <T> getObjectData(classOfT: Class<T>): T? {
        return try {
            Gson().fromJson(data, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> getObjectData(classOfT: Class<T>, defaultValue: T): T {
        return try {
            Gson().fromJson(data, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

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

    fun <T> getPageData(classOfT: Class<T>): Pagination.PageData<T> {
        var pageData = Pagination.PageData<T>()
        try {
            if (data != null) {
                val jsonObject = JsonParser().parse(data).asJsonObject
                val jsonArray = jsonObject.get("rows").asJsonArray
                pageData = Pagination.PageData(
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
}
