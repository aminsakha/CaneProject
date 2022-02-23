package com.caneproject

class Data(
    private var string1: String,
    private var string2: String,
    private var string3: String,
    private var string4: String,
) {
    fun setDataAttribute(counter: Int, attribute: String) {
        when (counter) {
            1 -> string1 = attribute
            2 -> string2 = attribute
            3 -> string3 = attribute
            4 -> string4 = attribute
        }
    }

    override fun toString(): String {
        return "$string1 , $string2 , $string3 , $string4 "
    }
}