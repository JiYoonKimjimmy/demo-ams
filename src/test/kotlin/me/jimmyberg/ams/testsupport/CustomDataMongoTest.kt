package me.jimmyberg.ams.testsupport

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.transaction.annotation.Transactional

@TestDatabaseConfiguration
@Transactional
@DataMongoTest
annotation class CustomDataMongoTest
