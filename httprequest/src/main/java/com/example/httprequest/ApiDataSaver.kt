package com.example.httprequest

interface ApiDataSaver {
    fun save(apiName: String, paramsHash: Int, saveData: String)
    fun find(apiName: String, paramsHash: Int): String?
}