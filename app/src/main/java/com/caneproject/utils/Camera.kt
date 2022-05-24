package com.caneproject.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Camera(private var myContext: Context, private var camera: PreviewView) {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(myContext)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(camera.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    myContext as LifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
            }
        }, ContextCompat.getMainExecutor(myContext))
    }

    fun takePhoto(myContext: Context): Uri? {
        Log.d("Check", "1 t")
        val imageCapture = imageCapture ?: return null
        Log.d("Check", "2 t")
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions =
            ImageCapture.OutputFileOptions
                .Builder(
                    myContext.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()
        var uriResult: Uri? = null

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(myContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("capture", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(
                        File(
                            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path}/CameraX-Image",
                            "${name}.jpg"
                        )
                    )
                    uriResult = savedUri
                    uriList.add(savedUri)
                    Log.d("Check", "took some")
                }
            }
        )
        return uriResult
    }

    fun showCamera() {
        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun turnOffCamera() {
        cameraExecutor.shutdown()
    }
}