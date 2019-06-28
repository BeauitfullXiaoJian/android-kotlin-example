package com.example.androidx_example.works

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.androidx_example.R
import com.example.androidx_example.until.TimeLock
import com.example.androidx_example.until.api.CODE_SUCCESS
import com.example.androidx_example.until.api.HttpRequest
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.getFileNameStrByTime
import com.example.androidx_example.until.getNewNotifyId
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

/**
 * 图片下载Worker
 */
class ImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val chanelId = "ImageDownloadWorker"
    private val notifyId = getNewNotifyId()

    override fun doWork(): Result {
        val imageUrl = inputData.getString(DOWNLOAD_IMAGE_URL)
        return imageUrl?.let {
            val res = HttpRequest.download(it)
            val notifyManagerCompat = NotificationManagerCompat.from(applicationContext)
            val notifyBuilder = createNotifyBuilder(applicationContext, chanelId)
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
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MICROSECONDS
                )
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(app).enqueue(workRequest)
            return WorkManager.getInstance(app).getWorkInfoByIdLiveData(workRequest.id)
        }

        /**
         * 保存文件
         */
        fun saveImage(res: Response, appContext: Context, progressCallback: (progress: Int) -> Unit): Result {
            val body = res.body()
            if (res.code() != CODE_SUCCESS || body == null) return Result.failure()

            var inputStream: InputStream? = null
            var outputSteam: OutputStream? = null
            return try {
                val saveFile = File(
                    appContext.getExternalFilesDir(null),
                    getFileNameStrByTime("jpg")
                )
                inputStream = body.byteStream()
                outputSteam = FileOutputStream(saveFile)
                val buffer = ByteArray(1024)
                val total = body.contentLength()
                var completed = 0
                var percent: Int
                var len: Int
                val timeLock = TimeLock.getDefaultLock()
                while (inputStream.read(buffer).also { len = it } > 0) {
                    completed += len
                    outputSteam.write(buffer, 0, len)
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
                    outputSteam?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        /**
         * 创建一个通知Builder
         */
        fun createNotifyBuilder(appContext: Context, chanelId: String): NotificationCompat.Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "下载任务"
                val descriptionText = "图片下载消息"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(chanelId, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            return NotificationCompat.Builder(appContext, chanelId)
                .setContentTitle("图片下载")
                .setContentText("正在下载.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
        }

//        fun getRectBitmap(inputStream: InputStream, appContext: Context) {
//            val bmpReg = BitmapRegionDecoder.newInstance(inputStream, true)
//            val bmp = bmpReg.decodeRegion(Rect(0, 0, 100, 100), BitmapFactory.Options())
//            val saveFile = File(
//                appContext.getExternalFilesDir(null),
//                getFileNameStrByTime("jpg")
//            )
//            val outputSteam = FileOutputStream(saveFile)
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputSteam)
//            outputSteam.close()
//            bmp.recycle()
//        }
    }
}