package me.jimmyberg.ams.v1.student.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.kotest.CustomBehaviorSpec
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.School

class FindStudentServiceTest : CustomBehaviorSpec({

    given("학생 정보 단건 조회 요청하여") {
        val document = studentDocumentFixture.make(
            name = "김모아",
            school = School("신길초", SchoolType.PRIMARY, 6)
        )
        studentRepository.save(document)

        `when`("'신길초' 학교의 '김모건' 이름과 일치한 학생 정보가 없다면") {
            val predicate = StudentPredicate(name = "김모건", school = School("신길초", SchoolType.PRIMARY, 6))
            val exception = shouldThrow<EntityNotFoundException> { studentRepository.findByPredicate(predicate) }

            then("EntityNotFoundException 예외 발생한다") {
                exception.message shouldBe "Student not found."
            }
        }

        `when`("'신길초' 학교의 '김모아' 이름과 일치한 학생 정보 있다면") {
            val predicate = StudentPredicate(name = "김모아", school = School("신길초", SchoolType.PRIMARY, 6))
            val result = studentRepository.findByPredicate(predicate)

            then("학생 정보 조회 성공 확인한다") {
                result shouldNotBe null
                result.name shouldBe "김모아"
            }
        }
    }

})