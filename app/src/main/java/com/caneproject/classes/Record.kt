package com.caneproject.classes

import com.caneproject.db.Data

data class Record(var dbList: MutableList<Data> , var dateAndTime:String) {
    override fun toString(): String {
        return dateAndTime
    }
}