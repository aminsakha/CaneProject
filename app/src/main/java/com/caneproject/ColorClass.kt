package com.caneproject

class ColorClass(
     var White: String,
     var Red: String,
     var Green: String,
     var Blue: String,
) {
    var resultColor: String=""
    fun setDataAttribute(counter: Int, attribute: String) {
        when (counter) {
            1 -> White = attribute
            2 -> Red = attribute
            3 -> Green = attribute
            4 -> Blue = attribute
            5 -> resultColor = attribute
        }
    }

    override fun toString(): String {
        return "$White , $Red , $Green , $Blue "
    }
}
