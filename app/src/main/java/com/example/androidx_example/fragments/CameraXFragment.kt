package com.example.androidx_example.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.content.ContextCompat

import com.example.androidx_example.R
import kotlinx.android.synthetic.main.fragment_camera_x.*

class CameraXFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera_x, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCameraX()
    }

    private fun prepareCameraX() {
        if (checkCameraPermission()) {
            val config = PreviewConfig.Builder().build()
            Preview(config).apply {
                setOnPreviewOutputUpdateListener { output ->
                    camera_preview_view.surfaceTexture = output.surfaceTexture
                }
                CameraX.bindToLifecycle(this@CameraXFragment, this)
            }

        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            Manifest.permission.CAMERA.hashCode()
        )
    }

    private fun checkCameraPermission(): Boolean {
        return arrayOf(Manifest.permission.CAMERA).all {
            ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
