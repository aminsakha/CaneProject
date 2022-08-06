package com.caneproject.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * the main work of this class is for making camera instance and save gotten pictures
 * and the specific of how this class work is said fully here : https://developer.android.com/codelabs/camerax-getting-started#0
 */
class Camera(
    myContext: Context,
    private var camera: PreviewView,
    textBox: TextView
) {
    private var weakReferenceContext: WeakReference<Context>? = null
    private var weakReferenceTextBox: WeakReference<TextView>? = null

    init {
        weakReferenceContext = WeakReference(myContext)
        weakReferenceTextBox= WeakReference(textBox)
    }

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(weakReferenceContext?.get()!!)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(camera.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    weakReferenceContext?.get() as LifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
            }
        }, weakReferenceContext!!.get()?.let { ContextCompat.getMainExecutor(it) })
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

    fun setTextBoxText(string: String) {
        "Data Number : $string".also { weakReferenceTextBox?.get()?.text = it }
    }
}