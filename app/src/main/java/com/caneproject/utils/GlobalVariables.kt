package com.caneproject.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.caneproject.db.Data
import com.caneproject.db.DataDb

var uriList = mutableListOf<Uri>()
var dateAndTime = ""
var dataList = mutableListOf<Data>()
lateinit var db: DataDb
var selectedItemInRecView = ""
var bluetoothAdapter: BluetoothAdapter? = null
var connectedDevice: BluetoothDevice? = null
var socket: BluetoothSocket? = null
