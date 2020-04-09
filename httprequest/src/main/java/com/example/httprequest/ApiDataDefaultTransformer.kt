package com.example.httprequest

import com.example.httprequest.HttpRequest.Companion.RESPONSE_DATA_ERROR
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser

class ApiDataDefaultTransformer : ApiDataTransformer {

    class TransformerData(val result: Boolean, val message: String, val data: Any?)

    override fun transform(body: String, code: Int): ApiData {
        return try {
            val json = Gson().fromJson(body, TransformerData::class.java)
            ApiData(
                result = json.result,
                message = json.message,
                data = json.data?.toString() ?: String()
            )
        } catch (e: Exception) {
            ApiData.dataError(body, e.toString()).storeException(e)
        }
    }
}