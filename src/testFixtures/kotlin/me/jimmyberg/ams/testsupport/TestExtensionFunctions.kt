package me.jimmyberg.ams.testsupport

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.jackson.plugin.JacksonPlugin
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin
import com.navercorp.fixturemonkey.kotest.KotestPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import java.util.*

object TestExtensionFunctions {

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .plugin(KotestPlugin())
        .plugin(JacksonPlugin())
        .plugin(JakartaValidationPlugin())
        .build()

    fun generateUUID(length: Int = 10): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length)
    }

    fun <T> T?.ifNotNullEquals(value: T): Boolean {
        return this?.let { this == value } ?: true
    }

}
