package com.caneproject

class Data(
    private val string1: String,
    private val string2: String,
    private val string3: String,
    private val string4: String
) {
    override fun toString(): String {
        return "$string1'W' , $string2'R', $string3'G', $string4'B'"
    }
}