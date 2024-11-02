package me.jimmyberg.ams.testsupport

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@TestDatabaseConfiguration
@DataMongoTest
annotation class CustomDataMongoTest
