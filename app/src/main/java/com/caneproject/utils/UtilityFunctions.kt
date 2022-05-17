package com.caneproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.caneproject.db.Data
import com.google.android.material.snackbar.Snackbar
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.*


var tmpArr = mutableListOf<String>()
fun toastShower(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun writeToFile(context: Context?, uri: Uri, content: String) {
    val parcelFileDescriptor =
        context?.contentResolver?.openFileDescriptor(uri, "w")
    val fileOutputStream = FileOutputStream(parcelFileDescriptor?.fileDescriptor)
    fileOutputStream.write(content.toByteArray())
    fileOutputStream.close()
    parcelFileDescriptor?.close()
}

fun saveToFile(
    context: Context?,
    list: MutableList<Data>,
    requestCode: Int,
    resultCode: Int,
    data: Intent?
) {
    val stringBuilder = StringBuilder()
    list.forEach {
        stringBuilder.append("$it\n")
    }
    if (resultCode == Activity.RESULT_OK && requestCode == 40 && data != null) {
        writeToFile(context, data.data!!, stringBuilder.toString())
        toastShower(context, "file saved successfully")
    }
}

fun processOnString(string: String): MutableList<String> {
    val validStrings = mutableListOf<String>()
    string.forEach { firstIt ->
        if (firstIt !in 'A'..'Z')
            tmpArr.add(firstIt.toString())
        else {
            var validAttribute = ""
            tmpArr.forEach {
                validAttribute += it
            }
            tmpArr.clear()
            validStrings.add("$validAttribute$firstIt")
        }
    }
    return validStrings
}

fun changeFragment(view: View, action: Int) = Navigation.findNavController(view).navigate(action)

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun showSnackBar(view: View, alertText: String, actionText: String, actionMethod: () -> (Unit)) {
    Snackbar.make(view, alertText, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText) {
            actionMethod()
        }
        .show()
}

@SuppressLint("MissingPermission")
fun showListViewInDialog(title: String, itemList: Array<CharSequence>, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setItems(
        itemList
    ) { _, item ->
        connectedDeviceAddress = (itemList[item] as String).split(",")[1]
        bluetoothAdapter?.bondedDevices?.forEach {
            if (it.address == connectedDeviceAddress)
                connectedDevice = it
            Log.d("Connectionn", "showListViewInDialog: $connectedDevice")
        }
    }
    builder.create().show()
}

fun currentDateAndTime() = DateFormat.getDateTimeInstance().format(Date()).substring(12) + " , " +
        JalaliDateTime.Now().toString().substring(0, 11)
