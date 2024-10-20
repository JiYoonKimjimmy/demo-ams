package me.jimmyberg.ams.testsupport

import io.kotest.core.spec.style.StringSpec

abstract class CustomStringSpec(body: BaseStringSpec.() -> Unit = {}) : BaseStringSpec() {
    init {
        body()
    }
}

abstract class BaseStringSpec : StringSpec() {
    val dependencies = TestDependencies
}