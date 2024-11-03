package me.jimmyberg.ams.testsupport

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@TestMongoDBConfiguration
@AutoConfigureMockMvc
@SpringBootTest
annotation class CustomSpringBootTest