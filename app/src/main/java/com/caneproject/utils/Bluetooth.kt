package com.caneproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.caneproject.R
import com.caneproject.fragment.cameraInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * this class is for creating bluetooth channel , connecting and receiving data
 * @constructor just get the context
 */
class Bluetooth(val context: Context) {
    /**
     * this function is for showing the old paired devices to the user and let the user select one
     */
    @SuppressLint("MissingPermission")
    fun chooseDevice() {
        val bluetoothManager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            (context as Activity).startActivityForResult(enableBtIntent, 1)
        } else {
            var pairedDevicesContent = arrayOf<CharSequence>()
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            for (i in pairedDevices!!.indices) {
                val deviceName = pairedDevices.elementAt(i).name
                val deviceHardwareAddress = pairedDevices.elementAt(i).address
                pairedDevicesContent += "$deviceName :\n$deviceHardwareAddress\n"
            }
            showListViewInDialog("Connect To Which One ?", pairedDevicesContent, context)
        }
    }

    @SuppressLint("MissingPermission")
    fun startConnection() {
        try {
            socket =
                connectedDevice?.createRfcommSocketToServiceRecord(
                    UUID.fromString(
                        context.getString(
                            R.string.uuid
                        )
                    )
                )
            bluetoothAdapter?.cancelDiscovery()
            socket?.connect()
        } catch (e: IOException) {
            simpleSnackBar(
                (context as Activity).findViewById(R.id.connectToDeviceBTN),
                "Try Again , Not Connected"
            )
        }
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
        }
    }

    /**
     * this function plays the main role in this class and kinda in the whole project , as it get what modulo sent to it
     * and every time check if its the time to close that specific object and add it to the list if its valid according to the
     * algorithms
     */
    @SuppressLint("RestrictedApi")
    fun receiveData() {
        var counter = 1
        val inputStream: InputStream = socket!!.inputStream
        var currentData = initialData()
        while (true) {
            try {
                val byteCount = inputStream.available()
                if (byteCount > 0) {
                    val rawBytes = ByteArray(byteCount)
                    inputStream.read(rawBytes)
                    val receivedString = String(rawBytes, StandardCharsets.UTF_8)
                    Log.d("Connection", receivedString)

                    if (counter > 8) {
                        currentData.dateAndTime = dateAndTime
                        dataListFromModulo.add(currentData)
                        Log.d("Res", currentData.resultColor.first().toString())
                        CoroutineScope(Dispatchers.Main).launch {
                            playMusic(context, currentData.resultColor.first().toString())
                        }
                        cameraInstance?.setTextBoxText(dataListFromModulo.size.toString())
                        currentData = initialData()
                        counter = 1
                    }
                    val curStatus: List<String> = processOnString(receivedString)
                    for (status in curStatus) {
                        if (counter == 1 && !status.endsWith("W")) continue
                        if (counter == 1 && status.endsWith("W")) {
                            cameraInstance?.takePhoto(context)
                        }
                        currentData.setDataAttribute(
                            counter,
                            status
                        )
                        counter++
                    }
                }
            } catch (e: IOException) {
            }
        }
    }
}
