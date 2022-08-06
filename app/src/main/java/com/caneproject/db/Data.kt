package com.caneproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * main class of room database that work with this 8 main items
 */
@Entity(tableName = "data_table")
data class Data(
    var White: String,
    var Red: String,
    var Green: String,
    var Blue: String,
    var k: String,
    var ir: String,
    var led: String,
    var resultColor: String,
    var dateAndTime: String,
    var uriString: String,
    var isTrueColor: Boolean,
    var trueColor: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    override fun toString(): String {
        return "$White , $Red , $Green , $Blue "
    }

    fun toStringForSecondPart(): String {
        return "$k , $ir , $led , $resultColor "
    }

    fun setDataAttribute(counter: Int, attribute: String) {
        when (counter) {
            1 -> White = attribute
            2 -> Red = attribute
            3 -> Green = attribute
            4 -> Blue = attribute
            5 -> k = attribute
            6 -> ir = attribute
            7 -> led = attribute
            8 -> resultColor = attribute
        }
    }
}
