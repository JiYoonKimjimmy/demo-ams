package me.jimmyberg.ams.testsupport

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["de.flapdoodle.mongodb.embedded.version=5.0.5"])
@DataMongoTest
annotation class CustomDataMongoTest
