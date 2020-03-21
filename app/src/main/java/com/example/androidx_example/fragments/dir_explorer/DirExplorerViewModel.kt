package com.example.androidx_example.fragments.dir_explorer

import android.util.Size
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.httprequest.Request

class DirExplorerViewModel : ViewModel() {

    // 当前文件夹文件列表
    val fileList by lazy {
        MutableLiveData<Array<FileItem>>().also { loadFileList() }
    }
    // 当前显示的文件夹
    val parentDirPath = MutableLiveData<String>().apply { value = String() }
    // 页面是刷新状态
    val refreshing = MutableLiveData<Boolean>().apply { value = false }

    /**
     * 加载文件夹文件列表
     */
    fun loadFileList() {
        refreshing.postValue(true)
        Request.get(
            apiName = "dir",
            params = hashMapOf("dir" to (this.parentDirPath.value ?: String())),
            successDo = { res ->
                val items = res.getObjectList(FileItem::class.java).toTypedArray()
                fileList.postValue(items)
            },
            completeDo = { refreshing.postValue(false) }
        )
    }

    data class ViewSize(val width: Int, val height: Int, val type: String) {
        fun toSize() = Size(width, height)
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
        val parentDir: String,
        // 预览尺寸
        val size: ViewSize?
    ) {
        val type: FileType
            get() = FileType.values().find {
                it.typeStr == fileType
            } ?: FileType.OTHER

        val visibility: Int
            get() {
                return when (type) {
                    FileType.DIR, FileType.OTHER -> View.VISIBLE
                    else -> View.GONE
                }
            }
    }

    enum class FileType(val typeStr: String) {
        PDF("pdf"),
        VIDEO("video"),
        IMAGE("img"),
        DIR("dir"),
        OTHER("other")
    }
}
