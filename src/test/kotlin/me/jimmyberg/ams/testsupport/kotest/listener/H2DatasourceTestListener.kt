package me.jimmyberg.ams.testsupport.kotest.listener

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import me.jimmyberg.ams.testsupport.config.TestDatasourceConfig

object H2DatasourceTestListener : TestListener {

    override suspend fun beforeTest(testCase: TestCase) {
        TestDatasourceConfig.connected()
        TestDatasourceConfig.setup()
    }

}