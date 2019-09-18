package com.example.androidx_example.until.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.androidx_example.App

class AppSQLiteHelp(context: Context) :
    SQLiteOpenHelper(context,
        SQLiteUntil.DATABASE_NAME, null,
        SQLiteUntil.DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            create table if not exists api_data(
                id integer primary key autoincrement,
                api_name text,
                api_param_hash text,
                api_data text,
                save_time integer,
                lost_time integer
            )""".trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists api_data")
        onCreate(db)
    }

    companion object {

        private val db: AppSQLiteHelp by lazy {
            AppSQLiteHelp(App.instance)
        }

        /**
         * 保存api数据到api_table中
         * @param apiName 接口的名称
         * @param hasCode 参数的hash码
         * @param dataStr 接口返回数据
         * @param effectiveTimeMillis 数据有效时间，毫秒
         */
        fun saveApiData(apiName: String, hasCode: Int, dataStr: String, effectiveTimeMillis: Long) {
            val nowMillis = System.currentTimeMillis()
            val values = ContentValues().apply {
                put("api_name", apiName)
                put("api_param_hash", hasCode)
                put("api_data", dataStr)
                put("save_time", nowMillis)
                put("lost_time", nowMillis + effectiveTimeMillis)
            }
            db.writableDatabase.insert("api_data", null, values)
        }

        /**
         * 获取保存的api数据
         * @param apiName 接口名称
         * @param hasCode 参数的hash码
         * @return 查询结果字符串，如果没有那么得到一个空String()
         */
        fun getSaveApiData(apiName: String, hasCode: Int): String {
            val nowMillis = System.currentTimeMillis()
            val cursor = db.readableDatabase.query(
                "api_data",
                arrayOf("api_data"),
                "api_name = ? and api_param_hash = ? and lost_time > ?",
                arrayOf(apiName, hasCode.toString(), nowMillis.toString()),
                null, // 参考SQL--Group
                null, // 参考SQL--Having
                "id desc", // 参考SQL--Order By
                "1" // 参考SQL--Limit 限制查询结果数目
            )
            return cursor?.let {
                val dataStr = if (it.moveToNext()) it.getString(cursor.getColumnIndex("api_data"))
                else String()
                return dataStr.also { cursor.close() }
            } ?: String()
        }
    }
}