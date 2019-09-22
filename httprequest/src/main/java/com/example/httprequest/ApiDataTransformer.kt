package com.example.httprequest

interface ApiDataTransformer {
    fun transform(body: String, code: Int): ApiData
}