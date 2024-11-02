package me.jimmyberg.ams.testsupport

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@TestDatabaseConfiguration
@AutoConfigureMockMvc
@SpringBootTest
annotation class CustomSpringBootTest