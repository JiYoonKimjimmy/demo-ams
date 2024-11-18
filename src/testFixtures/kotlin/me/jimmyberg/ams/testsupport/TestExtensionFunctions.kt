package me.jimmyberg.ams.testsupport

import java.util.*

object TestExtensionFunctions {

    fun generateUUID(length: Int = 10): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length)
    }

    fun <T> T?.ifNotNullEquals(value: T): Boolean {
        return this?.let { this == value } ?: true
    }

}
