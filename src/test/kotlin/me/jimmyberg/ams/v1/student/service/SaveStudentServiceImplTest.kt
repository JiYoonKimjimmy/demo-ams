package me.jimmyberg.ams.v1.student.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.StudentStatus.REGISTER_WAITING
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec

class SaveStudentServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture
    val fakeStudentRepository = dependencies.fakeStudentRepository

    val saveStudentService = dependencies.saveStudentService

    beforeSpec {
        fakeStudentRepository.clear()
    }

    given("학생 정보 등록 요청되어") {
        val name = "김모건"
        var domain = studentFixture.make(name = name)
        fakeStudentRepository.save(domain)

        `when`("동일한 'name' & 'phone' & 'birth' 등록된 학생 정보가 있다면") {
            val exception = shouldThrow<IllegalArgumentException> { saveStudentService.save(domain) }

            then("중복 등록 예외 발생 확인한다") {
                exception.message shouldBe "Student with 김모건, 01012340001, 19900309 already exists."
            }
        }

        `when`("이미 동일한 'name' 학생 정보 등록인 경우'") {
            domain = studentFixture.make(name = name, phone = "01012341234")

            val result = saveStudentService.save(domain)

            then("학샘 이름 index '2' 저장 정상 확인한다") {
                result.name shouldBe "김모건"
                result.indexOfName shouldBe 2
            }
        }

        `when`("신규 학생 정보 등록인 경우") {
            domain = studentFixture.make(name = "김모아", phone = "01012341234")

            val result = saveStudentService.save(domain)

            then("학생 정보를 저장 정상 확인한다") {
                result.name shouldBe "김모아"
                result.indexOfName shouldBe null
                result.status shouldBe REGISTER_WAITING
            }
        }
    }

})