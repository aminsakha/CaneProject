package com.caneproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoData {
    @Insert
    suspend fun addData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)

    @Query("SELECT * FROM data_table ORDER BY id ASC")
    suspend fun readAllData(): List<Data>

    @Query("UPDATE data_table SET trueColor =:newColor WHERE uriString =:uri")
    suspend fun updateColor(newColor: String, uri:String)

    @Query("DELETE FROM data_table")
    suspend fun deleteEntire()
    @Query("DELETE FROM data_table WHERE dateAndTime =:date ")
    suspend fun deleteData(date: String)

    @Query("SELECT DISTINCT dateAndTime FROM data_table")
    suspend fun getDates(): MutableList<String>

    @Query("SELECT * FROM data_table WHERE dateAndTime =:date ")
    suspend fun getRecordInThisDate(date: String): MutableList<Data>
}