package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caneproject.adaptors.HardWareModeAdaptor
import com.caneproject.classes.Data
import com.caneproject.databinding.FragmentGettingDataPageBinding
import com.caneproject.utils.MakeConnectionToModulo
import com.otaliastudios.cameraview.CameraView

var receivedNotes: MutableList<Data>? = null
var adapter: HardWareModeAdaptor? = null
var makeConnectionToModulo: MakeConnectionToModulo? = null
lateinit var camera: CameraView

class GettingDataPage : Fragment() {
    private var _binding: FragmentGettingDataPageBinding? = null
    private val binding get() = _binding!!
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
        camera = binding.camera
        camera.setLifecycleOwner(this)
//        if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)) {
//            makeConnectionToModulo =
//                MakeConnectionToModulo(myContext as Activity, myContext)
//            makeConnectionToModulo!!.execute()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}