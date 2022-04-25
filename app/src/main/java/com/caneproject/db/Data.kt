package com.caneproject.db

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val White: String,
    val Red: String,
    val Green: String,
    val Blue: String,
    val k: String,
    val ir: String,
    val led: String,
    val resultColor: String, var uri: Uri? = null
) {
}