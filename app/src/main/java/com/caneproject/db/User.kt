package com.caneproject.db

data class User(val userName: String, val passWord: CharArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (!passWord.contentEquals(other.passWord)) return false

        return true
    }

    override fun hashCode(): Int {
        return passWord.contentHashCode()
    }
}