package com.caneproject.classes

import android.net.Uri
import java.net.URI

class DataClass(
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


    override fun toString(): String {
        return "$White , $Red , $Green , $Blue "
    }

    fun toStringForSecondPart(): String {
        return "$k , $ir , $led , $resultColor "
    }
}
