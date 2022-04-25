package com.caneproject.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.caneproject.R
import com.caneproject.databinding.FragmentGettingDataPageBinding
import com.caneproject.utils.MakeConnectionToModulo
import com.caneproject.utils.changeFragment
import com.caneproject.utils.toastShower
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

var uriList = mutableListOf<Uri>()
var imageCapture: ImageCapture? = null
private lateinit var cameraExecutor: ExecutorService
var makeConnectionToModulo: MakeConnectionToModulo? = null
var _binding: FragmentGettingDataPageBinding? = null
val binding get() = _binding!!
var dateAndTime = JalaliDateTime.Now().toString().substring(0, 11) + "\n" +
        DateFormat.getDateTimeInstance().format(Date()).substring(12)

class GettingDataPage : Fragment() {

    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGettingDataPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)) {
            makeConnectionToModulo =
                MakeConnectionToModulo(myContext as Activity, myContext)
            makeConnectionToModulo!!.execute()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()

        binding.endBTN.setOnClickListener {
            disconnect()
            changeFragment(
                binding.endBTN,
                R.id.action_gettingDataPage_to_dataAnaliticsPage
            )
        }
    }

    private fun disconnect(): Boolean {
        if (makeConnectionToModulo?.socket != null) {
            makeConnectionToModulo?.socket?.close()
            makeConnectionToModulo = null
            return true
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(myContext)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.camera.surfaceProvider)
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
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e("capture", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(myContext))
    }

    fun takePhoto(myContext: Context): Uri? {
        val imageCapture = imageCapture ?: return null

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
        Log.d("beginListenForData", "output")
        var uriResult: Uri? = null

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(myContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("capture", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d("beginListenForData", "onSAved")
                    val savedUri = Uri.fromFile(
                        File(
                            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path}/CameraX-Image",
                            "${name}.jpg"
                        )
                    )
                    uriResult = savedUri
                    uriList.add(savedUri)
                    toastShower(myContext, "got  ${uriList.size}")
                }
            }
        )
        return uriResult
    }

}

fun takingPhoto(context: Context) {
    GettingDataPage().takePhoto(context)
    Log.d("beginListenForData", "into taking")
}

fun setTexts(string: String) {
    "Data Number : $string".also { binding.countBox.text = it }
}

