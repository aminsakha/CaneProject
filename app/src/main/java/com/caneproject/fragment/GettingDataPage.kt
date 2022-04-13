package com.caneproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.caneproject.databinding.FragmentGettingDataPageBinding
import com.caneproject.utils.MakeConnectionToModulo
import java.text.DateFormat
import java.util.*
import java.util.concurrent.ExecutorService

typealias LumaListener = (luma: Double) -> Unit

private var imageCapture: ImageCapture? = null
private lateinit var cameraExecutor: ExecutorService
private fun takePhoto() {}

var makeConnectionToModulo: MakeConnectionToModulo? = null
var _binding: FragmentGettingDataPageBinding? = null
val binding get() = _binding!!

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

        val tmp = JalaliDateTime.Now().toString().substring(0, 11) + "\n" +
                DateFormat.getDateTimeInstance().format(Date()).substring(11)
        binding.dateBox.text = tmp
//        if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)) {
//            makeConnectionToModulo =
//                MakeConnectionToModulo(myContext as Activity, myContext)
//            makeConnectionToModulo!!.execute()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}

fun setTexts(vararg string: String) {
    binding.countBox.text = string[0]
    binding.textView2.text = "${string[1]} , ${string[2]}"
    binding.textView3.text =
        "${string[3]} , ${string[4]}  ,${string[5]}  ,${string[6]} , ${string[7]} ,${string[8]} "
}


