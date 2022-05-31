package com.caneproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(val userName: String, val passWord: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}