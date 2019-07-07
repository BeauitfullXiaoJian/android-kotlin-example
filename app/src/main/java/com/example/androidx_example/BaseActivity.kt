package com.example.androidx_example

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController

open class BaseActivity : AppCompatActivity() {

    private val permissionCallbacks = hashMapOf<Int, () -> Unit>()
    private var _toolbar: Toolbar? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCallbacks.keys.contains(requestCode)) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                permissionCallbacks[requestCode]?.invoke()
            }
            permissionCallbacks.remove(requestCode)
        }
    }

    protected fun setUpActionBar(toolbar: Toolbar, navCtrl: NavController) {
        _toolbar = toolbar
        toolbar.setupWithNavController(navCtrl)
    }

    fun addPermissionRequest(requestCode: Int, successCall: () -> Unit) {
        permissionCallbacks[requestCode] = successCall
    }
}
