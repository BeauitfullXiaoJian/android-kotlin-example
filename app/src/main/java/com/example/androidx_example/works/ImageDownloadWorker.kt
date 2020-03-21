package com.example.androidx_example.works

import android.app.Application
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.androidx_example.R
import com.example.androidx_example.until.tool.TimeLock
import com.example.androidx_example.until.api.CODE_SUCCESS
import com.example.androidx_example.until.tool.debugInfo
import com.example.androidx_example.until.tool.getFileNameStrByTime
import com.example.androidx_example.until.ui.NotifyUntil
import com.example.httprequest.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * 图片下载Worker
 */
class ImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val imageUrl = inputData.getString(DOWNLOAD_IMAGE_URL)
        return imageUrl?.let {
            val res = Request.download(it)
            val notifyManagerCompat = NotificationManagerCompat.from(applicationContext)
            val notifyBuilder = NotifyUntil.getSimpleNotifyBuilder(
                appContext = applicationContext,
                notifyName = "下载任务",
                title = "图片下载",
                content = "正在下载.",
                iconId = R.mipmap.ic_launcher,
                descriptionText = "图片下载消息"
            )
            val notifyId = NotifyUntil.getCurrentNotifyId()
            saveImage(res, applicationContext) { progress ->
                when (progress) {
                    DOWNLOAD_COMPLETE_VALUE -> {
                        notifyBuilder.setProgress(0, 0, false)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText("下载成功")
                    }
                    DOWNLOAD_FAIL_VALUE -> {
                        notifyBuilder.setProgress(0, 0, true)
                            .setContentText("下载失败，请重试")
                    }
                    else -> {
                        notifyBuilder.setProgress(DOWNLOAD_COMPLETE_VALUE, progress, false)
                            .setContentText("当前进度:$progress")
                    }
                }
                debugInfo("下载进度", progress.toString())
                notifyManagerCompat.notify(notifyId, notifyBuilder.build())
            }
        } ?: Result.failure()
    }

    companion object {

        const val SAVE_IMAGE_PATH = "SAVE_IMAGE_PATH"
        private const val DOWNLOAD_IMAGE_URL = "DOWNLOAD_IMAGE_URL"
        private const val DOWNLOAD_COMPLETE_VALUE = 101
        private const val DOWNLOAD_FAIL_VALUE = -1

        /**
         * 执行图片下载任务，并返回任务信息的LiveData
         */
        fun execute(app: Application, imageUrl: String): LiveData<WorkInfo> {
            val inputData = Data.Builder()
                .putString(DOWNLOAD_IMAGE_URL, imageUrl)
                .build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<ImageDownloadWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
            val workManager = WorkManager.getInstance(app)
            workManager.enqueue(workRequest)
            return workManager.getWorkInfoByIdLiveData(workRequest.id)
        }

        /**
         * 保存文件
         */
        fun saveImage(
            res: Response,
            appContext: Context,
            progressCallback: (progress: Int) -> Unit
        ): Result {
            val body = res.body()
            if (res.code() != CODE_SUCCESS || body == null) return Result.failure()

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            return try {
                val saveFile = File(
                    appContext.getExternalFilesDir(null),
                    getFileNameStrByTime("jpg")
                )
                inputStream = body.byteStream()
                outputStream = FileOutputStream(saveFile)
                val buffer = ByteArray(1024)
                val total = body.contentLength()
                var completed = 0
                var percent: Int
                var len: Int
                val timeLock = TimeLock.getDefaultLock()
                while (inputStream.read(buffer).also { len = it } > 0) {
                    completed += len
                    outputStream.write(buffer, 0, len)
                    val currentPercent = (completed * 100 / total).toInt()
                    if (!timeLock.isLock) {
                        percent = currentPercent
                        progressCallback(percent)
                    }
                }
                progressCallback(DOWNLOAD_COMPLETE_VALUE)
                Result.success(
                    Data.Builder()
                        .putString(SAVE_IMAGE_PATH, saveFile.absolutePath)
                        .build()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                progressCallback(DOWNLOAD_FAIL_VALUE)
                Result.retry()
            } finally {
                try {
                    inputStream?.close()
                    outputStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}