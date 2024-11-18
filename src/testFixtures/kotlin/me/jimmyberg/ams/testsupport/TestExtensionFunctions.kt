package me.jimmyberg.ams.testsupport

import java.util.*
import java.util.concurrent.atomic.AtomicLong

object TestExtensionFunctions {

    fun generateUUID(length: Int = 10): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length)
    }

    private val sequence = AtomicLong()
    fun generateSequence(id: Long? = null): Long {
        return id ?: sequence.incrementAndGet()
    }

    fun <T> T?.ifNotNullEquals(value: T): Boolean {
        return this?.let { this == value } ?: true
    }

}
