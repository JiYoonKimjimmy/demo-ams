package me.jimmyberg.ams.testsupport.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.jimmyberg.ams.v1.parent.repository.entity.Parents
import me.jimmyberg.ams.v1.relation.repository.entity.StudentParents
import me.jimmyberg.ams.v1.student.repository.entity.Students
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class TestDatasourceConfig {

//    val database: Database by lazy {
//        HikariConfig()
//            .apply {
//                jdbcUrl = "jdbc:h2:mem:ams;MODE=MYSQL;DB_CLOSE_DELAY=-1"
//                driverClassName = "org.h2.Driver"
//                username = "sa"
//                password = ""
//            }
//            .let { HikariDataSource(it) }
//            .let { Database.connect(it) }
//    }

    companion object {
        lateinit var database: Database

        fun connected() {
            database = HikariConfig()
                .apply {
                    jdbcUrl = "jdbc:h2:mem:ams;MODE=MYSQL;DB_CLOSE_DELAY=-1"
                    driverClassName = "org.h2.Driver"
                    username = "sa"
                    password = ""
                }
                .let { HikariDataSource(it) }
                .let { Database.connect(it) }
        }

        fun setup() {
            transaction {
                val schemas = arrayOf(
                    Students,
                    Parents,
                    StudentParents
                )
                SchemaUtils.create(*schemas)
            }
        }
    }

}