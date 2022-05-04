package com.caneproject.classes

import android.net.Uri
import androidx.room.Room
import com.caneproject.db.Data
import com.caneproject.db.DataDb

var uriList = mutableListOf<Uri>()
var dateAndTime=""
var dataList = mutableListOf<Data>()
 lateinit var db: DataDb
var selectedItemInRecView=""
