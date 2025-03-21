package me.jimmyberg.ams.infra.config

import me.jimmyberg.ams.v1.student.repository.entity.StudentTable
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