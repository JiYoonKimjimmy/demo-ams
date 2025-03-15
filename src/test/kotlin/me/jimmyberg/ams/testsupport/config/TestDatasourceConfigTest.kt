package me.jimmyberg.ams.testsupport.config

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.v1.parent.repository.table.Parents
import me.jimmyberg.ams.v1.relation.repository.table.StudentParents
import me.jimmyberg.ams.v1.student.repository.table.Students
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class TestDatasourceConfigTest : CustomStringSpec({

    lateinit var database: Database

    beforeTest {
        database = TestDatasourceConfig().database
    }

    "Test Datasource 연결 테스트 성공 확인한다" {
        database.url shouldBe "jdbc:h2:mem:ams"
    }

    "Student 정보 생성하여 DB 저장 성공 확인한다" {
        transaction {
            addLogger(StdOutSqlLogger)

            // given
            // `Students` schema 생성
            SchemaUtils.create(Students)

            // `Students` insert
            val studentId = Students.insert {
                it[name] = "김모건"
                it[nameLabel] = null
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

    "Student & Parent 관계 저장하여 성공 정상 확인한다" {
        transaction {
            addLogger(StdOutSqlLogger)

            // given
            SchemaUtils.create(Students, Parents, StudentParents)

            val saveStudentId = Students.insertAndGetId {
                it[name] = "김모건"
                it[nameLabel] = null
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
            }

            val saveParentId1 = Parents.insertAndGetId {
                it[name] = "김지윤"
                it[phone] = "01012340002"
                it[gender] = "Male"
                it[status] = StudentStatus.ACTIVATED.name
            }

            val saveParentId2 = Parents.insertAndGetId {
                it[name] = "김수지"
                it[phone] = "01012340003"
                it[gender] = "Female"
                it[status] = StudentStatus.ACTIVATED.name
            }

            StudentParents.insert {
                it[studentId] = saveStudentId.value
                it[parentId] = saveParentId1.value
            }

            StudentParents.insert {
                it[studentId] = saveStudentId.value
                it[parentId] = saveParentId2.value
            }

            // when
            val parentCount = StudentParents.select(StudentParents.parentId)
                .where { StudentParents.studentId eq saveStudentId.value }
                .count()

            // then
            parentCount shouldBe 2
        }
    }

})