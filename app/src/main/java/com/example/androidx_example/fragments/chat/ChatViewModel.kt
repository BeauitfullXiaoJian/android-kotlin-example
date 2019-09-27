package com.example.androidx_example.fragments.chat

import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.example.androidx_example.until.sql.RoomUntil

class ChatViewModel : ViewModel() {

    val msgRows by lazy {
        RoomUntil.db.msgSaveDataDao().messages().toLiveData(
            pageSize = 3
        )
    }
}