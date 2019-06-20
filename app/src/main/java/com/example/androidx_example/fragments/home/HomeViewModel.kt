package com.example.androidx_example.fragments.home

import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.example.androidx_example.data.Pagination
import com.example.androidx_example.data.VideoDataSourceFactory
import com.example.androidx_example.until.api.HttpRequest

class HomeViewModel : ViewModel() {

    val videoRows by lazy {
        VideoDataSourceFactory().toLiveData(
            pageSize = Pagination.DEFAULT_PAGE_SIZE
        )
    }

    val recyclerPosition by lazy {
        MutableLiveData<RecyclerPositionData>().apply { value = RecyclerPositionData() }
    }

    val feedbackLabels by lazy {
        MutableLiveData<List<String>>().also { loadFeedbackLabels(it) }
    }

    private fun loadFeedbackLabels(labels: MutableLiveData<List<String>>) {
        labels.value = listOf(
            "血腥恐怖",
            "色情低俗",
            "封面恶心",
            "标题党/封面党"
        )
    }

//    companion object {

//        private lateinit var sVideoRowsInstance: LiveData<PagedList<Video>>
//        private lateinit var sRecyclerViewPosition: MutableLiveData<RecyclerPositionData>
//
//        fun getVideoRowsLiveData(): LiveData<PagedList<Video>> {
//            sVideoRowsInstance = if (::sVideoRowsInstance.isInitialized) sVideoRowsInstance
//            else VideoDataSourceFactory().toLiveData(
//                pageSize = 10
//            )
//            return sVideoRowsInstance
//        }
//
//        fun getRecyclerViewPosition(): MutableLiveData<RecyclerPositionData> {
//            sRecyclerViewPosition = if (::sRecyclerViewPosition.isInitialized) sRecyclerViewPosition
//            else MutableLiveData<RecyclerPositionData>().apply {
//                value = RecyclerPositionData()
//            }
//            return sRecyclerViewPosition
//        }
//    }
}