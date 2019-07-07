package com.example.androidx_example.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_data")
data class ApiSaveData(

    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo(name = "api_name")
    val apiName: String?,

    @ColumnInfo(name = "api_param_hash")
    val paramHash: Int?,

    @ColumnInfo(name = "api_data")
    val apiData: String?,

    @ColumnInfo(name = "save_time")
    val saveTime: Long?,

    @ColumnInfo(name = "lost_time")
    val lostTime: Long?
)