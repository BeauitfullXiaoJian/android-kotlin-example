package com.example.androidx_example.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_error_log")
data class AppErrorLog(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "current_time")
    val currentTime: Long,

    @ColumnInfo(name = "throwable_msg")
    val throwableMsg: String,

    @ColumnInfo(name = "stack_info")
    val stackIno: String
)