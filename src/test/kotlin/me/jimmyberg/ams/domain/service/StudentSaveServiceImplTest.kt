package me.jimmyberg.ams.domain.service

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus.REGISTER_WAITING
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec

class StudentSaveServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture

    val studentSaveService = dependencies.studentSaveService

    given("학생 정보 등록 요청되어") {
        val name = "김모건"
        var domain = studentFixture.make(name = name)
        studentSaveService.save(domain)

        `when`("이미 동일한 'name' 학생 정보 등록인 경우'") {
            domain = studentFixture.make(name = name, phone = "01012341234")

            val result = studentSaveService.save(domain)

            then("학샘 이름 index '2' 저장 정상 확인한다") {
                result.name shouldBe "김모건"
                result.nameLabel shouldBe 2
            }
        }

        `when`("신규 학생 정보 등록인 경우") {
            domain = studentFixture.make(name = "NEW_김모아", phone = "01012341234")

            val result = studentSaveService.save(domain)

            then("학생 정보를 저장 정상 확인한다") {
                result.name shouldBe "NEW_김모아"
                result.nameLabel shouldBe null
                result.status shouldBe REGISTER_WAITING
            }
        }
    }

})