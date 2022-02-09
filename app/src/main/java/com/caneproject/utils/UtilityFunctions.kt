package com.caneproject.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.caneproject.Data
import java.io.FileOutputStream

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