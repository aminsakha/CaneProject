package com.caneproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.caneproject.R
import com.caneproject.databinding.FragmentInitPageBinding
import com.caneproject.db.DataDb
import com.caneproject.utils.*
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*


private var ConnectSuccess = true
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
        val loadingDialog = LoadingDialog(myContext as Activity)
        db = Room.databaseBuilder(
            myContext.applicationContext,
            DataDb::class.java,
            "data_table"
        ).build()

        binding.startConnectionBTN.setOnClickListener {
            loadingDialog.startDialog()
            //  if (makeConnectionToModulo?.socket == null || (makeConnectionToModulo?.isBluetoothOn == false)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    connectToModulo()
                    withContext(Dispatchers.Main) {
                        try {
//                            if (continueConnection()) {
//                                loadingDialog.dismissDialog()
//                                changeFragment(
//                                    binding.startConnectionBTN,
//                                    R.id.action_initPage_to_gettingDataPage
//                                )
//                            } else {
//                                loadingDialog.dismissDialog()
//                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.libraryBTN.setOnClickListener {
            connectToModulo()
            //changeFragment(binding.libraryBTN, R.id.action_initPage_to_dataManaging)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToModulo() {
        try {
            val bluetoothManager: BluetoothManager =
                myContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                (myContext as Activity).startActivityForResult(enableBtIntent, 1)
            } else {
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                var pairedDevicesName = arrayOf<CharSequence>()
                for (i in pairedDevices!!.indices) {
                    val deviceName = pairedDevices.elementAt(i).name
                    val deviceHardwareAddress = pairedDevices.elementAt(i).address // MAC address
                    pairedDevicesName += "$deviceName \n $deviceHardwareAddress"
                }
                val builder = AlertDialog.Builder(myContext)
                builder.setTitle("Connect to which modulo ?")
                builder.setItems(
                    pairedDevicesName
                ) { _, item ->
                    Toast.makeText(
                        myContext,
                        pairedDevicesName[item],
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val alert: AlertDialog = builder.create()
                alert.show()
            }
//            if (socket == null || !isConnected) {
//                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//                val address = myContext.getString(R.string.Bluetooth_AddressNew)
//                val disposition = bluetoothAdapter?.getRemoteDevice(address)
//                socket =
//                    disposition?.createInsecureRfcommSocketToServiceRecord(
//                        UUID.fromString(
//                            myContext.getString(
//                                R.string.uuid
//                            )
//                        )
//                    )
//                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
//                socket?.connect()
//            }
        } catch (e: IOException) {
            ConnectSuccess = false
        }
    }

//    @SuppressLint("MissingPermission")
//    private fun continueConnection(): Boolean {
//        if (!ConnectSuccess && bluetoothAdapter!!.isEnabled) {
//            toastShower(myContext, "Connection Failed.Try again ")
//            return false
//        } else {
//            toastShower(myContext, "Connected")
//            isConnected = true
//            HandleReceivedNotes.beginListenForData(socket, myContext)
//        }
//        return true
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}