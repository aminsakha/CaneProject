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
import com.caneproject.R
import com.caneproject.classes.Data
import com.caneproject.fragment.imageCapture
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

fun takePhoto(myContext: Context): Uri? {
    val imageCapture = imageCapture ?: return null

    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }

    val outputOptions = myContext.let {
        ImageCapture.OutputFileOptions
            .Builder(
                it.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
    }
    var uriResult: Uri? = null
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(myContext),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("capture", "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                val savedUri = Uri.fromFile(
                    File(
                        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path}/CameraX-Image",
                        "${name}.jpg"
                    )
                )
                uriResult = savedUri
            }
        }
    )
    return uriResult
}