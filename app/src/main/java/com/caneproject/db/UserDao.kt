package com.caneproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
@Dao
interface UserDao {
    @Insert
    suspend fun addUser(data: User)

    @Delete
    suspend fun deleteUser(data: User)
}