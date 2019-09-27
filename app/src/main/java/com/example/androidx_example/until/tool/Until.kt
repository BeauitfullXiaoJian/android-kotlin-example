package com.example.androidx_example.until.tool

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidx_example.BaseActivity
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


// 每次允许显示TOAST的最小间隔
const val MIN_TOAST_TIME = 3000
// 当前Toast显示是的毫秒时间
var toastShowTime = 0L

/**
 * 显示提示消息
 * @param message String 消息内容
 */
fun showToast(message: String, activity: Activity?) {
    if (System.currentTimeMillis() - toastShowTime > MIN_TOAST_TIME) {
        activity?.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
        toastShowTime = System.currentTimeMillis()
    }
}

/**
 * 打印调试信息
 * @param messages String 消息内容,可以多个
 */
fun debugInfo(vararg messages: String) {
    Log.d("DebugInfo", messages.joinToString(","))
}

/**
 * 获取ViewModel实例
 */
fun <T : ViewModel> createViewModel(app: Application, modelClass: Class<T>): T {
    return ViewModelProvider.AndroidViewModelFactory.getInstance(app)
        .create(modelClass)
}

fun <T : ViewModel> createViewModel(activity: FragmentActivity, modelClass: Class<T>): T {
    return createViewModel(
        activity as AppCompatActivity,
        modelClass
    )
}

fun <T : ViewModel> createViewModel(activity: AppCompatActivity, modelClass: Class<T>): T {
    val app = activity.application
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app)
    return ViewModelProvider(activity.viewModelStore, factory).get(modelClass)
}


fun <T : ViewModel> createViewModel(fragment: Fragment, modelClass: Class<T>): T {
    val app = fragment.activity!!.application
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app)
    return ViewModelProvider(fragment.viewModelStore, factory).get(modelClass)
}

fun <T : ViewModel> createViewModel(modelClass: Class<T>): T {
    return ViewModelProvider.NewInstanceFactory().create(modelClass)
}

/**
 * 将dp转换为pxf
 */
fun dpToPx(dp: Int): Int {
    val res = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), res.displayMetrics)
        .toInt()
}

fun getPxFromDpIntegerId(res: Resources, id: Int): Int {
    val dp = res.getInteger(id)
    return dpToPx(dp)
}

/**
 *  小时转毫秒数
 */
fun hourToMillis(hour: Int): Long {
    return (hour * 60 * 60 * 1000).toLong()
}

/**
 * 按时间生成一个文件名
 */
fun getFileNameStrByTime(suffix: String): String {
    val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    return simpleDateFormat.format(Date()) + ".$suffix"
}


// 权限请求列表计数器
var requestCodeCx = 1000

/**
 * 请求权限
 */
fun requestPermission(activity: BaseActivity, permission: String, successDo: () -> Unit) {
    val hasPermission = ContextCompat.checkSelfPermission(activity, permission)
    if (hasPermission == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(
            activity,
            listOf(permission).toTypedArray(),
            ++requestCodeCx
        )
        activity.addPermissionRequest(requestCodeCx, successDo)
    } else {
        successDo()
    }
}

const val TEN_THOUSAND = 10000

/**
 * 把数字10000以上的变为"1万"形式的字符串，如果10000以下则直接转换为字符串
 */
fun tenThousandNumFormat(num: Int): String {
    return if (num > TEN_THOUSAND) "${String.format("%.1f", 1.0 * num / TEN_THOUSAND)}万"
    else num.toString()
}

fun shareImage(context: Context, bitmap: Bitmap) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(
            Intent.EXTRA_STREAM,
            getTempBitmapUri(context, bitmap)
        )
    }
    context.startActivity(Intent.createChooser(intent, "图片分享到"));
}

fun getTempSaveFileOutputStream(context: Context, suffix: String): OutputStream {
    val saveFile = File(
        context.getExternalFilesDir(null),
        getFileNameStrByTime(suffix)
    )
    return saveFile.outputStream()
}

fun getTempBitmapUri(context: Context, bitmap: Bitmap): Uri {
    val imageDir = File(context.filesDir, "images").also { it.mkdir() }
    val file = File(imageDir, getFileNameStrByTime("jpg"))
    val outStream = file.outputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
    outStream.close()
    bitmap.recycle()
    return FileProvider.getUriForFile(context, "com.example.cool1024.file", file)
}
