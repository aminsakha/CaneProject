package com.caneproject.db

import androidx.room.Delete
import androidx.room.Insert

interface UserDao {
    @Insert
    suspend fun addUser(data: User)

    @Delete
    suspend fun deleteUser(data: User)
}