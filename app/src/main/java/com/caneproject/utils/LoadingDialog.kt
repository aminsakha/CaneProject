package com.caneproject.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.caneproject.R

/**
 * this class used for making simple loading dialog
 */
class LoadingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null
    fun startDialog() {
        val builder = AlertDialog.Builder(
            activity
        )
        val layoutInflater = activity.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun dismissDialog() {
        alertDialog!!.dismiss()
    }
}