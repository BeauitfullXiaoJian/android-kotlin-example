package com.example.androidx_example.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.httprequest.Request

class DirExplorerViewModel : ViewModel() {

    // 当前文件夹文件列表
    val fileList by lazy {
        MutableLiveData<List<FileItem>>().also { loadFileList() }
    }
    // 当前显示的文件夹
    val parentDirPath = MutableLiveData<String>().apply { value = String() }
    // 页面是刷新状态
    val refreshing = MutableLiveData<Boolean>().apply { value = false }

    /**
     * 加载文件夹文件列表
     */
    private fun loadFileList() {
        Request.get(
            apiName = "dir",
            params = hashMapOf("dir" to parentDirPath.value!!),
            successDo = { res -> fileList.postValue(res.getObjectList(FileItem::class.java)) },
            completeDo = { refreshing.postValue(false) }
        )
    }

    data class FileItem(
        // 文件地址
        val filePath: String,
        // 文件类型
        val fileType: String,
        // 文件预览地址
        val previewUrl: String,
        // 文件下载地址
        val downloadUrl: String,
        // 所属目录地址
        val parentDir: String
    )
}
