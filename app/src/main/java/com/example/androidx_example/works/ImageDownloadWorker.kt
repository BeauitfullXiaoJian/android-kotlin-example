package com.example.androidx_example.works

import android.app.Application
import android.content.Context
import androidx.work.*
import com.example.androidx_example.until.api.HttpRequest
import java.io.InputStream
import java.util.*

/**
 * 图片下载Worker
 */
class ImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val imageUrl = inputData.getString(DOWNLOAD_IMAGE_URL)
        return imageUrl?.let {
            HttpRequest.download(it).subscribe(
                { inputStream -> saveImage(inputStream) },
                {},
                {})
            return@let Result.success()
        } ?: Result.failure()
    }

    companion object {

        private const val DOWNLOAD_IMAGE_URL = "DOWNLOAD_IMAGE_URL"

        /**
         * 执行图片下载任务，并返回任务的唯一UUID
         */
        fun execute(app: Application, imageUrl: String): UUID {
            val inputData = Data.Builder()
                .putString(DOWNLOAD_IMAGE_URL, imageUrl)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(app).enqueue(workRequest)
            return workRequest.id
        }

        /**
         * 保存文件
         */
        fun saveImage(inputStream: InputStream) {

        }
    }
}