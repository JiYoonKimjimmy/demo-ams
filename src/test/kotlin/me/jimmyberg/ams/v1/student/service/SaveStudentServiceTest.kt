package me.jimmyberg.ams.v1.student.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus.REGISTER_WAITING
import me.jimmyberg.ams.testsupport.CustomBehaviorSpec
import me.jimmyberg.ams.v1.student.service.domain.School
import me.jimmyberg.ams.v1.student.service.domain.Student

class SaveStudentServiceTest : CustomBehaviorSpec({

    val studentMapper = dependencies.studentMapper
    val studentRepository = dependencies.fakeStudentRepository
    val saveStudentService = dependencies.saveStudentService

    beforeSpec {
        studentRepository.clear()
    }

    given("'김모건' 이란 학생 정보를 저장하는 경우") {
        val name = "김모건"

        val student = Student(
            name = name,
            phone = "01012341234",
            birth = "19900309",
            gender = Gender.MALE,
            address = Address("12345", "Hello", "World"),
            school = School("신길초", SchoolType.PRIMARY, 6),
        )

        studentRepository.save(studentMapper.domainToDocumentV1(student))

        `when`("동일한 'name' & 'phone' & 'birth' 등록된 학생 정보가 있다면") {
            val exception = shouldThrow<IllegalArgumentException> { saveStudentService.save(student) }

            then("중복 등록 예외 응답 처리한다.") {
                exception.message shouldBe "Student with 김모건, 01012341234, 19900309 already exists."
            }
        }

        `when`("이미 동일한 'name' 학생 정보가 있다면'") {
            val domain = Student(
                name = name,
                phone = "01012345678",
                birth = "19900309",
                gender = Gender.MALE,
                school = School("신길초", SchoolType.PRIMARY, 6),
            )

            val result = saveStudentService.save(domain)

            then("학생 표시 이름을 '김모건2' 로 저장하고 정상 응답 처리한다.") {
                result.name shouldBe "김모건"
                result.indexOfName shouldBe 2
            }
        }

        `when`("정상적인 학생 정보라면") {
            val domain = Student(
                name = "김모아",
                phone = "01012345678",
                birth = "19900309",
                gender = Gender.MALE,
                school = School("신길초", SchoolType.PRIMARY, 6),
            )

            val result = saveStudentService.save(domain)

            then("학생 정보를 저장하고 정상 응답 처리한다.") {
                result.name shouldBe "김모아"
                result.indexOfName shouldBe null
                result.status shouldBe REGISTER_WAITING
            }
        }
    }
})