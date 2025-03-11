package me.jimmyberg.ams.testsupport.kotest

import io.kotest.core.spec.style.StringSpec
import me.jimmyberg.ams.testsupport.TestDependencies

abstract class CustomStringSpec(body: BaseStringSpec.() -> Unit = {}) : BaseStringSpec() {
    init {
        body()
    }
}

abstract class BaseStringSpec : StringSpec() {
    val dependencies = TestDependencies
}