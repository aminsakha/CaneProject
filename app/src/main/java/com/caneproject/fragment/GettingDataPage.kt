//package com.caneproject.fragment
//
//import android.app.Activity
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.ali.uneversaldatetools.date.JalaliDateTime
//import com.caneproject.BlankFragment
//import com.caneproject.R
//import com.caneproject.classes.Data
//import com.caneproject.databinding.FragmentGettingDataPageBinding
//import com.caneproject.utils.MakeConnectionToModulo
//import com.caneproject.utils.changeFragment
//import com.caneproject.utils.takePhoto
//import java.text.DateFormat
//import java.util.*
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
////var imageCapture: ImageCapture? = null
//private lateinit var cameraExecutor: ExecutorService
////var makeConnectionToModulo: MakeConnectionToModulo? = null
//var _binding: FragmentGettingDataPageBinding? = null
//val binding get() = _binding!!
//
//class GettingDataPage : Fragment() {
//
//    private lateinit var myContext: Context
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentGettingDataPageBinding.inflate(layoutInflater, container, false)
//        if (container != null)
//            myContext = container.context
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        val tmp = JalaliDateTime.Now().toString().substring(0, 11) + "\n" +
////                DateFormat.getDateTimeInstance().format(Date()).substring(12)
////        binding.dateBox.text = tmp
////        if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)) {
////            makeConnectionToModulo =
////                MakeConnectionToModulo(myContext as Activity, myContext)
////            makeConnectionToModulo!!.execute()
////        }
////        startCamera()
////        binding.dateBox.setOnClickListener {
////            disconnect()
////        }
////        cameraExecutor = Executors.newSingleThreadExecutor()
////        binding.countBox.setOnClickListener {changeFragment(binding.dateBox, R.id.action_gettingDataPage_to_initPage) }
//    }
//
////    private fun disconnect(): Boolean {
////        if (makeConnectionToModulo?.socket != null) {
////            makeConnectionToModulo?.socket?.close()
////            makeConnectionToModulo = null
////            return true
////        }
////        return false
////    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        cameraExecutor.shutdown()
//        Log.d("onDestroyView", "onDestroyView: went")
//        _binding = null
//    }
//
////    private fun startCamera() {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance(myContext)
////
////        cameraProviderFuture.addListener({
////            // Used to bind the lifecycle of cameras to the lifecycle owner
////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
////
////            // Preview
////            val preview = Preview.Builder()
////                .build()
////                .also {
////                    it.setSurfaceProvider(binding.camera.surfaceProvider)
////                }
////
////            imageCapture = ImageCapture.Builder()
////                .build()
////
////            // Select back camera as a default
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
////
////            try {
////                // Unbind use cases before rebinding
////                cameraProvider.unbindAll()
////
////                // Bind use cases to camera
////                cameraProvider.bindToLifecycle(
////                    this, cameraSelector, preview, imageCapture
////                )
////
////            } catch (exc: Exception) {
////                Log.e("capture", "Use case binding failed", exc)
////            }
////        }, ContextCompat.getMainExecutor(myContext))
////    }
//
//}
//
