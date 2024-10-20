package me.jimmyberg.ams.testsupport

import io.kotest.core.spec.style.BehaviorSpec

abstract class CustomBehaviorSpec(body: BaseBehaviorSpec.() -> Unit = {}) : BaseBehaviorSpec() {
    init {
        body()
    }
}

abstract class BaseBehaviorSpec : BehaviorSpec() {
    val dependencies = TestDependencies
}