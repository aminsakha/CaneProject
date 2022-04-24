package com.caneproject.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.caneproject.BlankFragment
import com.caneproject.classes.Data
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
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

