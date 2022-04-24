//package com.caneproject
//
//import android.app.Activity
//import android.content.ContentValues
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.caneproject.databinding.FragmentBlankBinding
//import com.caneproject.utils.HandleReceivedNotes
//import com.caneproject.utils.MakeConnectionToModulo
//import com.caneproject.utils.toastShower
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//var _binding: FragmentBlankBinding? = null
//val binding get() = _binding!!
//var imageCapture: ImageCapture? = null
//
//class BlankFragment : Fragment() {
//
//    private lateinit var cameraExecutor2: ExecutorService
//
//    var makeConnectionToModulo2: MakeConnectionToModulo? = null
//    private lateinit var myContext: Context
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentBlankBinding.inflate(layoutInflater, container, false)
//        if (container != null)
//            myContext = container.context
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        if (makeConnectionToModulo2?.socket == null || (makeConnectionToModulo2?.isBluetoothOn == false)) {
//            makeConnectionToModulo2 =
//                MakeConnectionToModulo(myContext as Activity, myContext)
//            makeConnectionToModulo2!!.execute()
//        }
//        cameraExecutor2 = Executors.newSingleThreadExecutor()
//        startCamera()
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(myContext)
//
//        cameraProviderFuture.addListener({
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Preview
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(binding.tmpCamera.surfaceProvider)
//                }
//
//            imageCapture = ImageCapture.Builder()
//                .build()
//
//            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll()
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview, imageCapture
//                )
//
//            } catch (exc: Exception) {
//                Log.e("capture", "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(myContext))
//    }
//

//fun setTexts(string: String) {
//    binding.countBox2.text = string
//
//}