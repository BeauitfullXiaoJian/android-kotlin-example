package com.example.androidx_example.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat

import com.example.androidx_example.R
import com.example.androidx_example.until.RequestCodes
import kotlinx.android.synthetic.main.fragment_camera_x.*
import java.util.*
import java.util.concurrent.Executor

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
            camera_preview_view.bindToLifecycle(this@CameraXFragment)
            camera_preview_view.takePicture(
                Executor { },
                object : ImageCapture.OnImageCapturedListener() {
                    override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                        
                    }
                })
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            RequestCodes.CAMERA
        )
    }

    private fun checkCameraPermission(): Boolean {
        return arrayOf(Manifest.permission.CAMERA).all {
            ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
