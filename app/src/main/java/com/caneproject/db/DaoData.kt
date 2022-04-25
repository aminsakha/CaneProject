package com.caneproject.db

import androidx.lifecycle.LiveData
import androidx.room.Query

interface DaoData {

    fun addData(data: Data) {}

    @Query("SELECT * FROM data_table ORDER BY id ASC")
    fun readData(): LiveData<List<Data>>
}