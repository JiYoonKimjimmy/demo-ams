package me.jimmyberg.ams.v1.student.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.domain.School
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.repository.fixture.StudentRepositoryFixture

class SaveStudentServiceTest : BehaviorSpec({

    val studentRepository = StudentRepositoryFixture()

    given("'김모건' 이란 학생 정보를 저장하는 경우") {
        val domain = Student(
            name = "김모건",
            phone = "01012341234",
            birthday = "19900309",
            gender = Gender.MALE,
            school = School("신길초", SchoolType.PRIMARY, 6),
            status = StudentStatus.REGISTER_WAITING
        )
        `when`("동일한 이름 & 생년월일 & 휴대폰번호 등록된 학생 정보가 있다면") {
            then("중복 등록 예외 응답 처리한다.") {
            }
        }
        `when`("이미 등록된 동일한 학생 이름이 있다면'") {
            then("학생 표시 이름을 '김모건2' 로 저장하고 정상 응답 처리한다.") {
            }
        }
        `when`("유효하고 정상적인 학생 정보라면") {
            val result = studentRepository.saveStudent(domain)
            then("학생 정보를 저장하고 정상 응답 처리한다.") {
                result.name shouldBe domain.name
            }
        }
    }
})