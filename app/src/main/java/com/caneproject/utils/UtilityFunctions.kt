package com.caneproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.navigation.Navigation
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.caneproject.BuildConfig
import com.caneproject.R
import com.caneproject.activities.screenHeight
import com.caneproject.activities.screenWidth
import com.caneproject.db.Data
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.material.snackbar.Snackbar
import com.google.common.reflect.Reflection.getPackageName
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.*
import java.security.AccessController.getContext
import java.text.DateFormat
import java.util.*


var tmpArr = mutableListOf<String>()
fun toastShower(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * this function is used for decode what modulo sent to us
 * @param string row string of what modulo sent
 * @return a list of string that can be 0 or more item such 0R,4G
 */
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

/**
 * this function is used for showing the user the paired devices with bluetooth
 * @param title title of dialog
 * @param itemList the CharSequence of what should be shown in the dialog
 */
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

fun sharePdf(listOfUris: ArrayList<Uri>, context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, listOfUris)
        type = "application/pdf"
    }
    startActivity(context, Intent.createChooser(shareIntent, null), null)
}

fun findMusicFileInDirectory(context: Context, fileName: String): MediaItem {
    val mp3Name = "b$fileName"
    val id = context.resources.getIdentifier(
        mp3Name,
        "raw",
        context.packageName
    )
    val uri = RawResourceDataSource.buildRawResourceUri(id)
    return MediaItem.fromUri(uri)
}

fun playMusic(context: Context?, fileName: String) {
    val playWhenReady = true
    val currentWindow = 0
    val playbackPosition: Long = 0
    val player: ExoPlayer = ExoPlayer.Builder(context!!).build()
    val myMedia = findMusicFileInDirectory(context, fileName)
    player.setMediaItem(myMedia)
    player.playWhenReady = playWhenReady
    player.seekTo(currentWindow, playbackPosition)
    player.prepare()
    player.play()
    player.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED)
                player.release()
        }
    })
}
