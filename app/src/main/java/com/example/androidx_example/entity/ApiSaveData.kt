package com.example.androidx_example.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_data")
data class ApiSaveData(

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private var id: Int,

    @ColumnInfo(name = "api_name")
    private var apiName: String,

    @ColumnInfo(name = "api_param_hash")
    private var hashCode: Int,

    @ColumnInfo(name = "save_time")
    private var saveTime: Int,

    @ColumnInfo(name = "lost_time")
    private var lostTime: Int

)