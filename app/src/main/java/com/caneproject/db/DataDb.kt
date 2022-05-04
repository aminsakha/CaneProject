package com.caneproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Data::class], version = 1)
abstract class DataDb : RoomDatabase() {
    abstract fun dataDao(): DaoData
}