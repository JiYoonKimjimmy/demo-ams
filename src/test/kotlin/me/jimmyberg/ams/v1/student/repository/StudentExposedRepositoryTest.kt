package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import org.jetbrains.exposed.sql.transactions.transaction

class StudentExposedRepositoryTest : CustomStringSpec({

    listeners(H2DatasourceTestListener)

    val studentExposedRepository = StudentExposedRepository()
    val studentFixture = dependencies.studentFixture

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

    "Student 학생 정보 `id` 컬럼 기준 DB 조회 성공 정상 확인한다" {
        transaction {
            // given
            val studentId = studentExposedRepository.save(studentFixture.make()).id.value.toString()

            // when
            val result = studentExposedRepository.findById(studentId)

            // then
            result!! shouldNotBe null
            result.id shouldNotBe null
        }
    }

    "Student 학생 정보 DB 변경 성공 정상 확인한다" {
        transaction {
            // given


            // when

            // then
        }
    }

    "Student 학생 정보 DB 삭제 성공 정상 확인한다" {
        transaction {
            // given


            // when

            // then
        }
    }

})