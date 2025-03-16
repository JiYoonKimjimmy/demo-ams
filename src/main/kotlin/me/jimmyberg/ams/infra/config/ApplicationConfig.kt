package me.jimmyberg.ams.infra.config

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Configuration

@ImportAutoConfiguration(ExposedAutoConfiguration::class)
@Configuration
class ApplicationConfig