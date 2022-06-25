package com.caneproject.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.caneproject.R
import com.caneproject.activities.screenHeight
import com.caneproject.activities.screenWidth
import com.caneproject.databinding.FragmentGettingDataPageBinding
import com.caneproject.db.Data
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.utils.AppCenterLog
import java.io.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


var tmpArr = mutableListOf<String>()
fun toastShower(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

fun simpleSnackBar(view: View, alertText: String) {
    Snackbar.make(view, alertText, Snackbar.LENGTH_SHORT).show()
}

@SuppressLint("MissingPermission")
fun showListViewInDialog(title: String, itemList: Array<CharSequence>, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setItems(
        itemList
    ) { _, item ->
        connectedDevice = bluetoothAdapter?.bondedDevices?.elementAt(item)
    }
    builder.create().show()
}

fun currentDateAndTime() = DateFormat.getDateTimeInstance().format(Date()).substring(12) + " , " +
        JalaliDateTime.Now().toString().substring(0, 11)

fun loadImageForRecView(context: Context, uri: Uri?, imageView: ImageView) {
    Glide.with(context).load(uri).placeholder(R.drawable.placeholder).override(200, 200)
        .dontAnimate()
        .apply(RequestOptions.bitmapTransform(RoundedCorners(15))).override(
            screenWidth / 4,
            (screenHeight / 2 * 0.9).toInt()
        ).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView)
}

fun initialData(): Data {
    return Data("", "", "", "", "", "", "", "", dateAndTime, "", true, "")
}

fun shareImages(listOfUris: ArrayList<Uri>, context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, listOfUris)
        type = "*/*"
    }
    startActivity(context, Intent.createChooser(shareIntent, null), null)
}

fun dataListIntoJson(dataList: MutableList<Data>): String {
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    return gsonPretty.toJson(dataList)
}

fun readFile(uri: Uri, myContext: Context): List<String> {
    val inputStream: InputStream? = myContext.contentResolver.openInputStream(uri)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val lineList = mutableListOf<String>()
    bufferedReader.forEachLine { lineList.add(it) }
    inputStream?.close()
    return lineList
}

fun jsonFileToObjectList(jsonString: String): MutableList<Data> {
    val arrayTutorialType = object : TypeToken<MutableList<Data>>() {}.type
    val gson = Gson()
    return gson.fromJson(jsonString, arrayTutorialType)
}

fun getUriForSharing(uri: String, context: Context): Uri {
    return Uri.parse(uri).path?.let { it1 -> File(it1) }?.let { it2 ->
        FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName.toString() + ".provider",
            it2
        )
    }!!
}