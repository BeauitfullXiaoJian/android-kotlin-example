package com.example.androidx_example

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private val permissionCallbacks = hashMapOf<Int, () -> Unit>()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCallbacks.keys.contains(requestCode)) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                permissionCallbacks[requestCode]?.invoke()
            }
            permissionCallbacks.remove(requestCode)
        }
    }

    fun addPermissionRequest(requestCode: Int, successCall: () -> Unit) {
        permissionCallbacks[requestCode] = successCall
    }
}
