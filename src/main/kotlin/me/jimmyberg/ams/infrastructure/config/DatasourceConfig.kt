package me.jimmyberg.ams.infrastructure.config

import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional

@Transactional
@Configuration
class DatasourceConfig : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        SchemaUtils.create(StudentTable)
    }

}