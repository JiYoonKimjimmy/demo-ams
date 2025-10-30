package me.jimmyberg.ams.testsupport.config

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity.StudentTable
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class TestDatasourceConfigTest : CustomStringSpec({

    listeners(H2DatasourceTestListener)

    "Test Datasource 연결 테스트 성공 확인한다" {
        // given
        val config = TestDatasourceConfig

        // when
        val database = config.database

        // then
        database.url shouldBe "jdbc:h2:mem:ams"
    }

    "Student 정보 생성하여 DB 저장 성공 확인한다" {
        transaction {
            addLogger(StdOutSqlLogger)

            // given
            // `Students` schema 생성
            SchemaUtils.create(StudentTable)

            // `Students` insert
            val studentId = StudentTable.insert {
                it[name] = "김모건"
                it[nameLabel] = null
                it[phone] = "01012340001"
                it[birth] = "19900309"
                it[gender] = Gender.MALE
                it[zipCode] = "12345"
                it[baseAddress] = "서울시 강서구"
                it[detailAddress] = "여기는 우리집"
                it[schoolName] = "신길초"
                it[schoolType] = SchoolType.PRIMARY
                it[grade] = 6
                it[status] = ActivationStatus.ACTIVATED
            } get StudentTable.id

            // when
            val result = StudentTable.select(StudentTable.id, StudentTable.name)
                .where(StudentTable.id eq studentId)
                .single()

            // then
            result[StudentTable.id] shouldBe studentId
            result[StudentTable.name] shouldBe "김모건"
        }
    }

    

})