package com.caneproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.db.DataDb
import com.caneproject.utils.*
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

private var ConnectSuccess = true
var bluetoothAdapter: BluetoothAdapter? = null
var socket: BluetoothSocket? = null
private var isConnected = false
private var isBluetoothOn = false

class InitPage : Fragment() {
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
        var loadingDialog = LoadingDialog(myContext as Activity)
        db = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()

        binding.ConnectionButton.setOnClickListener {
            loadingDialog.startDialog()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    connectToModulo()
                    withContext(Dispatchers.Main) {
                        try {
                            if (continueConnection()) {
                                loadingDialog.dismissDialog()
                                changeFragment(
                                    binding.ConnectionButton,
                                    R.id.action_initPage_to_gettingDataPage
                                )
                            } else
                                loadingDialog.dismissDialog()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.fileManagerBTN.setOnClickListener {
            changeFragment(binding.fileManagerBTN, R.id.action_initPage_to_dataManaging)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToModulo() {
        try {
            if (socket == null || !isConnected) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val address = myContext.getString(R.string.Bluetooth_AddressNew)
                val disposition = bluetoothAdapter?.getRemoteDevice(address)
                socket =
                    disposition?.createInsecureRfcommSocketToServiceRecord(
                        UUID.fromString(
                            myContext.getString(
                                R.string.uuid
                            )
                        )
                    )
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                socket?.connect()
            }
        } catch (e: IOException) {
            ConnectSuccess = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun continueConnection(): Boolean {
        if (!ConnectSuccess && bluetoothAdapter!!.isEnabled) {
            toastShower(myContext, "Connection Failed.Try again ")
            return false
        } else if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            (myContext as Activity).startActivityForResult(enableBtIntent, 1)
            isBluetoothOn = true
            return false
        } else {
            toastShower(myContext, "Connected")
            isConnected = true
            HandleReceivedNotes.beginListenForData(socket, myContext)
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}