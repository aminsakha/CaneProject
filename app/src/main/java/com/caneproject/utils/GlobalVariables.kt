package com.caneproject.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.net.Uri
import com.caneproject.db.Data
import com.caneproject.db.DataDb
//the list that camera uses to store uri in it and after that set them to the dataListFromModulo.uri
var uriList = mutableListOf<Uri>()
var dateAndTime = ""
//its the main list that every object got from modulo is stored here
var dataListFromModulo = mutableListOf<Data>()
lateinit var db: DataDb
// the selected item by the user in dataManaging class
var selectedItemInRecView = ""
// its the list that shows the items that user wants to delete in dataManaging class
var deletedItemsDate = mutableListOf<String>()
var bluetoothAdapter: BluetoothAdapter? = null
var connectedDevice: BluetoothDevice? = null
var socket: BluetoothSocket? = null
// the boolean that says whether user want to delete items in dataManaging class
var selectMultipleRow=false
