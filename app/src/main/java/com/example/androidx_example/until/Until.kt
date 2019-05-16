package com.example.androidx_example.until

import android.app.Activity
import android.util.Log
import android.widget.Toast
import io.reactivex.disposables.Disposable

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
 */
fun debugInfo(message: String) {
    Log.d("DebugInfo", message)
}