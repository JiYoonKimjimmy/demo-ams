package me.jimmyberg.ams.testsupport.config

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.v1.parent.repository.entity.ParentEntity
import me.jimmyberg.ams.v1.relation.repository.entity.StudentParentEntity
import me.jimmyberg.ams.v1.student.repository.entity.StudentEntity
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
            SchemaUtils.create(StudentEntity)

            // `Students` insert
            val studentId = StudentEntity.insert {
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
                it[status] = ActivationStatus.ACTIVATED
            } get StudentEntity.id

            // when
            val result = StudentEntity.select(StudentEntity.id, StudentEntity.name)
                .where(StudentEntity.id eq studentId)
                .single()

            // then
            result[StudentEntity.id] shouldBe studentId
            result[StudentEntity.name] shouldBe "김모건"
        }
    }

    "Student & Parent 관계 저장하여 성공 정상 확인한다" {
        transaction {
            addLogger(StdOutSqlLogger)

            // given
            SchemaUtils.create(StudentEntity, ParentEntity, StudentParentEntity)

            val saveStudentId = StudentEntity.insertAndGetId {
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
                it[status] = ActivationStatus.ACTIVATED
            }

            val saveParentId1 = ParentEntity.insertAndGetId {
                it[name] = "김지윤"
                it[phone] = "01012340002"
                it[gender] = "Male"
                it[status] = ActivationStatus.ACTIVATED
            }

            val saveParentId2 = ParentEntity.insertAndGetId {
                it[name] = "김수지"
                it[phone] = "01012340003"
                it[gender] = "Female"
                it[status] = ActivationStatus.ACTIVATED
            }

            StudentParentEntity.insert {
                it[studentId] = saveStudentId.value
                it[parentId] = saveParentId1.value
            }

            StudentParentEntity.insert {
                it[studentId] = saveStudentId.value
                it[parentId] = saveParentId2.value
            }

            // when
            val parent = StudentParentEntity.selectAll().where { StudentParentEntity.studentId eq saveStudentId.value }.toList()

            // then
            parent.size shouldBe 2
        }
    }

})