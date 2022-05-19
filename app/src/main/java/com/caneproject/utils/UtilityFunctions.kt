package com.caneproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.caneproject.R
import com.caneproject.activities.screenHeight
import com.caneproject.activities.screenWidth
import com.caneproject.db.Data
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.util.*


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