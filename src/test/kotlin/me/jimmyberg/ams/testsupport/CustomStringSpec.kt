package me.jimmyberg.ams.testsupport

import io.kotest.core.spec.style.StringSpec
import me.jimmyberg.ams.common.TestDependencies

abstract class CustomStringSpec(body: BaseStringSpec.() -> Unit = {}) : BaseStringSpec() {
    init {
        body()
    }
}

abstract class BaseStringSpec : StringSpec() {
    val dependencies = TestDependencies
}