package com.example.androidx_example.until

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidx_example.BaseActivity
import com.example.androidx_example.fragments.home.HomeViewModel
import io.reactivex.disposables.Disposable
import java.lang.reflect.Type
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
    activity: Activity?,
    successDo: (res: HttpRequest.ApiData) -> Unit
): Disposable = HttpRequest.post(apiName, params).subscribe {
    if (it.isOk()) successDo(it) else showToast(it.getMessage(), activity)
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
            Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        toastShowTime = System.currentTimeMillis();
    }
}

/**
 * 打印调试信息
 * @param message String 消息内容
 */
fun debugInfo(message: String) {
    Log.d("DebugInfo", message)
}

/**
 * 获取ViewModel实例
 */
fun <T : ViewModel> createViewModel(app: Application, modelClass: Class<T>): T {
    return ViewModelProvider.AndroidViewModelFactory.getInstance(app)
        .create(modelClass)
}

/**
 * 将dp转换为px
 */
fun dpToPx(dp: Int): Int {
    val res = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), res.displayMetrics)
        .toInt()
}

//fun getPxFromDpIntegerId(res: Resources, id: Int): Int {
//    val dp = res.getInteger(id)
//    return dpToPx(dp)
//}

/**
 * 请求权限
 */
var requestCodeCx = 1000;
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