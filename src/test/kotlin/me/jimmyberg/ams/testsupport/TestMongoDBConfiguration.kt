package me.jimmyberg.ams.testsupport

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = [
    "de.flapdoodle.mongodb.embedded.version=5.0.5",
    "spring.data.mongodb.port=0"
])
annotation class TestMongoDBConfiguration