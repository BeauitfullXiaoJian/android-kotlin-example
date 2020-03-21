package com.example.androidx_example.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat

import com.example.androidx_example.R
import com.example.androidx_example.until.RequestCodes
import com.example.androidx_example.until.tool.getFileNameStrByTime
import com.example.androidx_example.until.ui.AnimateUntil
import com.example.androidx_example.until.ui.ViewUntil
import kotlinx.android.synthetic.main.fragment_camera_x.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
            btn_capture.setOnClickListener { takePhoto() }
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

    private fun takePhoto() {
        val saveFile = File(activity!!.getExternalFilesDir(null), getFileNameStrByTime("jpg"))
        camera_preview_view.takePicture(
            saveFile,
            Executors.newSingleThreadExecutor(),
            imageSavedListener
        )
    }

    private val imageSavedListener = object : ImageCapture.OnImageSavedCallback {


        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            this@CameraXFragment.showPhotoView(
                BitmapFactory.decodeFile(outputFileResults.savedUri!!.path)
            )
            this@CameraXFragment.showToast(resources.getString(R.string.image_saved_success))
        }

        override fun onError(exception: ImageCaptureException) {
            this@CameraXFragment.showToast(exception.toString())
        }

    }


    private fun showPhotoView(bitmap: Bitmap) {
        photo_view.post {
            photo_view_container.apply {
                layoutParams.apply {
                    width = 0
                    height = 0
                }
                translationX = 0f
                translationY = 0f
                scaleX = 1f
                scaleY = 1f
                AnimateUntil.setSizeAndToTopStart(
                    this,
                    Size(ViewUntil.dpToPx(200), ViewUntil.dpToPx(120)),
                    Point(20, 20),
                    500
                )
                photo_view.setImageBitmap(bitmap)
            }
        }
    }
}
