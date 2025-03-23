package me.jimmyberg.ams.testsupport.kotest.listener

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import me.jimmyberg.ams.testsupport.config.TestDatasourceConfig

object H2DatasourceTestListener : TestListener {

    override suspend fun beforeSpec(spec: Spec) {
        TestDatasourceConfig.connected()
        TestDatasourceConfig.setup()
    }

}