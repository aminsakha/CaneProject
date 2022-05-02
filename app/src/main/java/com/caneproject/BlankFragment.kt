package com.caneproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.caneproject.databinding.FragmentBlankBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



class BlankFragment : Fragment() {
    private var imageCapture2: ImageCapture? = null
    private lateinit var cameraExecutor2: ExecutorService
    private lateinit var myContext: Context
    var _binding: FragmentBlankBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlankBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera2()
        } else {
            ActivityCompat.requestPermissions(
                myContext as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        cameraExecutor2 = Executors.newSingleThreadExecutor()
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            myContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun startCamera2() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(myContext)
        Log.d("capture", "startCamera: 102")
        cameraProviderFuture.addListener({
            Log.d("capture", "startCamera: 104")
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            Log.d("capture", "startCamera: 106")
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.camera2.surfaceProvider)
                }
            Log.d("capture", "startCamera: 114")
            imageCapture2 = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            Log.d("capture", "startCamera: 120")
            try {
                // Unbind use cases before rebinding
                Log.d("capture", "startCamera: 123")
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture2
                )
                Log.d("capture", "startCamera: 130")

            } catch (exc: Exception) {
                Log.e("capture", "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(myContext))
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}