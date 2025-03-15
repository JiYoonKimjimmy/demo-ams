package me.jimmyberg.ams.testsupport.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class TestDatasourceConfig {

    val database: Database by lazy {
        HikariConfig()
            .apply {
                jdbcUrl = "jdbc:h2:mem:ams;MODE=MYSQL;DB_CLOSE_DELAY=-1"
                driverClassName = "org.h2.Driver"
                username = "sa"
                password = ""
            }
            .let { HikariDataSource(it) }
            .let { Database.connect(it) }
    }

}