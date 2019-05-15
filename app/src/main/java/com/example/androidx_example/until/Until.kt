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

fun showToast(message: String, activity: Activity?) {
    activity?.runOnUiThread {
        Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}