package com.caneproject.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.db.DataDb
import com.caneproject.db.UserDb
import com.caneproject.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * landing page
 */
class InitPage : Fragment() {
    private var bluetoothInstance: Bluetooth? = null
    private var _binding: FragmentInitPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var myContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDb = Room.databaseBuilder(
            requireContext(),
            UserDb::class.java,
            "user_table"
        ).build()
        lifecycleScope.launch {
            if (userDb.userDao().getUser().isEmpty()){
                Log.d("check", "onCreate: got it")
                isUserSet=false
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (!isUserSet){
            findNavController().navigate(R.id.action_initPage_to_loginFragment, null)
            Log.d("check", "onCreate: got it 2")
        }
        _binding = FragmentInitPageBinding.inflate(layoutInflater, container, false)
        if (container != null)
            myContext = container.context
        Log.d("check", "$isUserSet in view")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = LoadingDialog(myContext as Activity)
        bluetoothInstance = Bluetooth(myContext)
        checkConnectivity()
        dataDb = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()

        binding.chooseDeviceBTN.setOnClickListener {
            bluetoothInstance?.chooseDevice()
        }
        /**
         * this function is using coroutine to handle AsyncTask process
         */
        binding.connectToDeviceBTN.setOnClickListener {
            if (connectedDevice == null) {
                toastShower(myContext, "please first select a device")
            } else {
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
