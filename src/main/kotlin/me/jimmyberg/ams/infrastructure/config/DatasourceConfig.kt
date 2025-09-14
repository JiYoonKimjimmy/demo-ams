package me.jimmyberg.ams.infrastructure.config

import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity.StudentTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration

@Configuration
class DatasourceConfig : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        transaction {
            SchemaUtils.create(StudentTable)
        }
    }

}