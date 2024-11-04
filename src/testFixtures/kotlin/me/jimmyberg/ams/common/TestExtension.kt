package me.jimmyberg.ams.common

import java.util.*

object TestExtension {

    fun generateUUID() = UUID.randomUUID().toString()

    fun <T> T?.ifNotNullEquals(value: T): Boolean {
        return this?.let { this == value } ?: true
    }

}
