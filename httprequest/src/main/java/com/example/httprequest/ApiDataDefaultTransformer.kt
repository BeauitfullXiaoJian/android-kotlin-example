package com.example.httprequest

import com.example.httprequest.HttpRequest.Companion.RESPONSE_DATA_ERROR
import com.google.gson.JsonElement
import com.google.gson.JsonParser

class ApiDataDefaultTransformer : ApiDataTransformer {

    private val JsonElement.asOnlyString: String
        get() = when {
            isJsonArray || isJsonObject -> toString()
            else -> asString
        }

    override fun transform(body: String, code: Int): ApiData {
        val jsonObject = JsonParser().parse(body).asJsonObject
        return try {
            ApiData(
                result = jsonObject.get(API_DATA_RESULT_KEY_STR).asBoolean,
                message = jsonObject.get(API_DATA_MESSAGE_KEY_STR).asOnlyString,
                data = jsonObject.get(API_DATA_DATA_KEY_STR)?.asOnlyString
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ApiData(
                result = false,
                message = RESPONSE_DATA_ERROR,
                data = body
            )
        }
    }

    companion object {
        const val API_DATA_RESULT_KEY_STR = "result"
        const val API_DATA_MESSAGE_KEY_STR = "message"
        const val API_DATA_DATA_KEY_STR = "data"
    }
}