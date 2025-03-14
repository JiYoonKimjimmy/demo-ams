package me.jimmyberg.ams.infra.datasource

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.v1.student.repository.table.Students
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedTest : CustomStringSpec({

    lateinit var database: Database

    beforeSpec {
        database = Database.connect(url = "jdbc:h2:mem:ams;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }

    "Exposed Database 연결 테스트 성공 확인한다" {
        database.url shouldBe "jdbc:h2:mem:ams;DB_CLOSE_DELAY=-1"
    }

    "Student 정보 생성하여 DB 저장 성공 확인한다" {
        transaction {
            addLogger(StdOutSqlLogger)

            // given
            // `Students` schema 생성
            SchemaUtils.create(Students)

            // `Students` insert
            val studentId = Students.insert {
                it[name] = "김지윤"
                it[indexOfName] = 0
                it[phone] = "01012340001"
                it[birth] = "19900309"
                it[gender] = "Male"
                it[zipCode] = "12345"
                it[baseAddress] = "서울시 강서구"
                it[detailAddress] = "여기는 우리집"
                it[schoolName] = "신길초"
                it[schoolType] = SchoolType.PRIMARY.name
                it[grade] = 6
                it[status] = StudentStatus.ACTIVATED.name
            } get Students.id

            // when
            val result = Students.select(Students.id, Students.name)
                .where(Students.id eq studentId)
                .single()

            // then
            result[Students.id] shouldBe studentId
            result[Students.name] shouldBe "김지윤"
        }
    }

})