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
                result = jsonObject.get("result").asBoolean,
                message = jsonObject.get("message").asOnlyString,
                data = jsonObject.get("data")?.asOnlyString
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
}