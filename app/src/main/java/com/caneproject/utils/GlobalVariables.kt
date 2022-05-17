package com.caneproject.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.net.Uri
import androidx.room.Room
import com.caneproject.db.Data
import com.caneproject.db.DataDb

var uriList = mutableListOf<Uri>()
var dateAndTime=""
var dataList = mutableListOf<Data>()
 lateinit var db: DataDb
var selectedItemInRecView=""
var connectedDeviceAddress=""
var bluetoothAdapter: BluetoothAdapter? = null
var connectedDevice: BluetoothDevice?=null
