package com.caneproject.classes

import android.net.Uri
import java.net.URI

class Data(
    var White: String,
    var Red: String,
    var Green: String,
    var Blue: String,
    var k: String,
    var ir: String,
    var led: String,
    var resultColor: String,
) {
    var uri: Uri?=null
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

    override fun toString(): String {
        return "$White , $Red , $Green , $Blue "
    }

    fun toStringForSecondPart(): String {
        return "$k , $ir , $led , $resultColor "
    }
}
