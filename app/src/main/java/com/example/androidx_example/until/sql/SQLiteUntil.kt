package com.example.androidx_example.until.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase

object SQLiteUntil {

    // 数据的名称，这个名称会作为数据存储文件的名称
    const val DATABASE_NAME = "www-cool1024-com.db"
    // SQLite数据库版本
    const val DATABASE_VERSION = 1
    // 数据库实例
    private lateinit var db: SQLiteDatabase

    /**
     * 开启数据库连接
     */
    fun openDB(context: Context) {
        if (!this::db.isInitialized || !db.isOpen) {
            db = context.openOrCreateDatabase(
                DATABASE_NAME,
                Context.MODE_PRIVATE,
                null
            )
        }
    }

    /**
     * 关闭数据库连接
     */
    fun closeDB() {
        if (this::db.isInitialized) {
            db.close()
        }
    }

    /**
     * 创建API数据保存表
     */
    fun createApiDataTable() {
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

    /**
     * 保存api数据到api_table中
     * @param apiName 接口的名称
     * @param hasCode 参数的hash码
     * @param dataStr 接口返回数据
     * @param effectiveTimeMillis 数据有效时间，毫秒
     */
    fun saveApiData(apiName: String, hasCode: Int, dataStr: String, effectiveTimeMillis: Long) {
        val nowMillis = System.currentTimeMillis()
        db.execSQL(
            "insert into api_data(api_name,api_param_hash,api_data,save_time,lost_time) values (?,?,?,?,?)",
            arrayOf(apiName, hasCode, dataStr, nowMillis, nowMillis + effectiveTimeMillis)
        )
    }


    /**
     * 获取保存的api数据
     * @param apiName 接口名称
     * @param hasCode 参数的hash码
     * @return 查询结果字符串，如果没有那么得到一个空String()
     */
    fun getSaveApiData(apiName: String, hasCode: Int): String {
        val nowMillis = System.currentTimeMillis()
        val cursor = db.rawQuery(
            "select api_data from api_data where api_name = ? and api_param_hash = ? and lost_time > ? order by id desc limit 1",
            arrayOf(apiName, hasCode.toString(), nowMillis.toString())
        )
        return (if (cursor.moveToNext()) {
            cursor.getString(cursor.getColumnIndex("api_data"))
        } else String()).also { cursor.close() }
    }
}