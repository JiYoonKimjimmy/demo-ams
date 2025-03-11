package me.jimmyberg.ams.testsupport.kotest

import io.kotest.core.spec.style.BehaviorSpec
import me.jimmyberg.ams.testsupport.TestDependencies

abstract class CustomBehaviorSpec(body: BaseBehaviorSpec.() -> Unit = {}) : BaseBehaviorSpec() {
    init {
        body()
    }
}

abstract class BaseBehaviorSpec : BehaviorSpec() {
    val dependencies = TestDependencies
}