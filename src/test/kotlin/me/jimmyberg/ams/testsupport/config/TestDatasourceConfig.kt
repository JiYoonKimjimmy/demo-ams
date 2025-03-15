package me.jimmyberg.ams.testsupport.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object TestDatasourceConfig {

    var database: Database

    init {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:ams;MODE=MYSQL;DB_CLOSE_DELAY=-1"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
        }
        database = Database.connect(datasource = HikariDataSource(hikariConfig))
    }

}