package me.jimmyberg.ams.testsupport.annotation

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@TestMongoConfiguration
@AutoConfigureMockMvc
@SpringBootTest
annotation class CustomSpringBootTest