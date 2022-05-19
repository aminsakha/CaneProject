package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.db.DataDb
import com.caneproject.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InitPage : Fragment() {
    private var bluetoothInstance: Bluetooth? = null
    private var _binding: FragmentInitPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = LoadingDialog(myContext as Activity)
        bluetoothInstance = Bluetooth(myContext)

        checkConnectivity()

        db = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()
        binding.chooseDeviceBTN.setOnClickListener {
            bluetoothInstance?.chooseDevice()
        }
        binding.connectToDeviceBTN.setOnClickListener {
            loadingDialog.startDialog()
            CoroutineScope(Dispatchers.IO).launch {
                bluetoothInstance?.startConnection()
                if (socket!!.isConnected) {
                    withContext(Dispatchers.Main) {
                        try {
                            loadingDialog.dismissDialog()
                            changeFragment(
                                binding.connectToDeviceBTN,
                                R.id.action_initPage_to_gettingDataPage
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    bluetoothInstance?.receiveData()
                } else
                    loadingDialog.dismissDialog()
            }
        }
        binding.libraryBTN.setOnClickListener {
            changeFragment(binding.libraryBTN, R.id.action_initPage_to_dataManaging)
        }
    }

    private fun checkConnectivity() {
        if (socket != null && socket!!.isConnected) {
            bluetoothInstance!!.disconnect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
