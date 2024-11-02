package me.jimmyberg.ams.testsupport

import io.kotest.core.spec.style.BehaviorSpec
import me.jimmyberg.ams.common.TestDependencies

abstract class CustomBehaviorSpec(body: BaseBehaviorSpec.() -> Unit = {}) : BaseBehaviorSpec() {
    init {
        body()
    }
}

abstract class BaseBehaviorSpec : BehaviorSpec() {
    val dependencies = TestDependencies
}