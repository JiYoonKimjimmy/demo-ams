package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class StudentExposedRepositoryTest : CustomStringSpec({

    listeners(H2DatasourceTestListener)

    val studentExposedRepository = dependencies.studentExposedRepository
    val studentMapper = dependencies.studentMapper
    val studentFixture = dependencies.studentFixture

    lateinit var saved: Student
    beforeTest {
        transaction {
            saved = studentExposedRepository.save(studentFixture.make()).let(studentMapper::entityToDomain)
        }
    }

    "Student 학생 정보 DB 저장 성공 정상 확인한다" {
        transaction {
            // given
            val student = studentFixture.make()

            // when
            val result = studentExposedRepository.save(student)

            // then
            result.id shouldNotBe null
            result.name shouldBe student.name
        }
    }

    "Student 학생 정보 'id' 컬럼 조건 DB 조회 성공 정상 확인한다" {
        transaction {
            // given
            val studentId = saved.id!!

            // when
            val result = studentExposedRepository.findById(studentId)

            // then
            result!! shouldNotBe null
            result.id shouldNotBe null
        }
    }

    "Student 학생 정보 컬럼 조건 없이 DB 조회 결과 'null' 성공 정상 확인한다" {
        transaction {
            // given
            val predicate = StudentPredicate()

            // when
            val result = studentExposedRepository.findByPredicate(predicate)

            // then
            result shouldBe null
        }
    }

    "Student 학생 정보 'name', 'phone', 'birth' 컬럼 조건 DB 조회 성공 정상 확인한다" {
        transaction {
            // given
            val predicate = StudentPredicate(name = saved.name, phone = saved.phone, birth = saved.birth)

            // when
            val result = studentExposedRepository.findByPredicate(predicate)

            // then
            result!! shouldNotBe null
            result.id.value.toString() shouldBe saved.id
            result.name shouldBe saved.name
            result.phone shouldBe saved.phone
            result.birth shouldBe saved.birth
        }
    }

    "Student 학생 정보 DB 변경 성공 정상 확인한다" {
        transaction {
            // given
            val updated = saved.copy(
                name = "김모아",
                gender = Gender.FEMALE
            )

            // when
            val result = studentExposedRepository.update(updated)

            // then
            result.id.toString() shouldBe saved.id
            result.name shouldBe "김모아"
            result.gender shouldBe Gender.FEMALE
        }
    }

    "Student 학생 정보 DB 삭제 성공 정상 확인한다" {
        transaction {
            // given
            val studentId = saved.id!!

            // when
            studentExposedRepository.delete(studentId)

            // then
            studentExposedRepository.findById(studentId) shouldBe null
        }
    }

})