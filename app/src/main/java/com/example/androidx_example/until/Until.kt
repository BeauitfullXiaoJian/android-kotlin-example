package com.example.androidx_example.until

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidx_example.BaseActivity
import com.example.androidx_example.until.api.HttpRequest
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * 发送一个POST请求，并剔除掉错误的消息
 * @param apiName String 接口名称
 * @param params HashMap<String, Any> 请求参数
 * @param activity? Activity 当前活动，如果提供了这个参数，将显示错误提示消息
 * @param successDo (res: HttpRequest.ApiData) -> Unit 成功回调方法
 */
fun postSuccess(
    apiName: String,
    params: HashMap<String, Any>,
    activity: Activity? = null,
    successDo: (res: HttpRequest.ApiData) -> Unit,
    completeDo: ((result: Boolean) -> Unit)? = null
): Disposable = HttpRequest.post(apiName, params).subscribe {
    if (it.isOk()) successDo(it) else showToast(it.getMessage(), activity)
    completeDo?.invoke(it.isOk())
}

/**
 * 发送一个GET请求，并剔除掉错误的消息
 * @param apiName String 接口名称
 * @param params HashMap<String, Any> 请求参数
 * @param activity? Activity 当前活动，如果提供了这个参数，将显示错误提示消息
 * @param successDo (res: HttpRequest.ApiData) -> Unit 成功回调方法
 */
fun getSuccess(
    apiName: String,
    params: HashMap<String, Any>,
    activity: Activity? = null,
    successDo: (res: HttpRequest.ApiData) -> Unit,
    completeDo: ((result: Boolean) -> Unit)? = null
): Disposable = HttpRequest.get(apiName, params).subscribe {
    if (it.isOk()) successDo(it) else showToast(it.getMessage(), activity)
    completeDo?.invoke(it.isOk())
}

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
    return createViewModel(activity as AppCompatActivity, modelClass)
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

/**
 * 获取一个NotifyId
 */
var notifyIdCx = 1000

fun getNewNotifyId(): Int {
    return notifyIdCx++
}
