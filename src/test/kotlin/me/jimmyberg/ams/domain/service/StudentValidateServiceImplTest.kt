package me.jimmyberg.ams.domain.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec

class StudentValidateServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture

    val studentSaveService = dependencies.studentSaveService
    val studentValidateService = dependencies.studentValidateService

    given("학생 정보 검증 요청되어") {
        val name = "김모건"
        val student = studentFixture.make(name = name)
        studentSaveService.save(student)

        `when`("동일한 'name' & 'phone' & 'birth' 등록된 학생 정보가 있다면") {
            val exception = shouldThrow<IllegalArgumentException> { studentValidateService.validate(student) }

            then("중복 등록 예외 발생 결과 확인한다") {
                exception.message shouldBe "Student with 김모건, 01012340001, 19900309 already exists."
            }
        }

        val newStudent = studentFixture.make(name = "NEW_김모건", phone = "01012341234")

        `when`("중복 정보 없는 신규 학생 정보인 경우") {
            then("중복 등록 예외 발생 없이 정상 결과 확인한다") {
                shouldNotThrowAny { studentValidateService.validate(newStudent) }
            }
        }
    }

})
