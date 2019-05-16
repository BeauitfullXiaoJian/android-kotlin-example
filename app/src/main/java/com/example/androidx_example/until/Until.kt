package com.example.androidx_example.until

import android.app.Activity
import android.widget.Toast
import io.reactivex.disposables.Disposable

const val MIN_TOAST_TIME = 3000
var toastShowTime = 0L

fun postSuccess(
    apiName: String,
    params: HashMap<String, Any>,
    activity: Activity?,
    successDo: (res: HttpRequest.ApiData) -> Unit
): Disposable = HttpRequest.post(apiName, params).subscribe {
    if (it.isOk()) successDo(it) else showToast(it.getMessage(), activity)
}

fun showToast(message: String, activity: Activity?) {
    if (System.currentTimeMillis() - toastShowTime > MIN_TOAST_TIME) {
        activity?.runOnUiThread {
            Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        toastShowTime = System.currentTimeMillis();
    }
}