package me.jimmyberg.ams.domain.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.ActivationStatus.REGISTER_WAITING
import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.InvalidRequestException
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec

class StudentSaveServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture

    val studentSaveService = dependencies.studentSaveService

    given("학생 정보 저장 요청되어") {
        val duplicateStudentName = "김중복"
        val duplicateStudent = studentFixture.make(name = duplicateStudentName)
        studentSaveService.save(duplicateStudent)

        `when`("신규 학생 정보 저장 요청이나, 동일한 학생 정보 이미 등록되어 있는 경우") {
            val duplicateStudent = studentFixture.make(name = duplicateStudentName)
            val exception = shouldThrow<InvalidRequestException> {
                studentSaveService.save(duplicateStudent)
            }

            then("'STUDENT_INFO_DUPLICATED' 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.STUDENT_INFO_DUPLICATED
            }
        }

        val name = "김모건"
        var student = studentFixture.make(name = name)
        studentSaveService.save(student)

        `when`("이미 동일한 'name' 학생 정보 등록인 경우'") {
            student = studentFixture.make(name = name, phone = "01012341234")

            val result = studentSaveService.save(student)

            then("학생 `nameLabel` 정보 '2' 저장 정상 확인한다") {
                result.name shouldBe "김모건"
                result.nameLabel shouldBe 2
            }
        }

        `when`("신규 학생 정보 등록인 경우") {
            student = studentFixture.make(name = "NEW_김모아", phone = "01012341234")

            val result = studentSaveService.save(student)

            then("학생 정보를 저장 정상 확인한다") {
                result.name shouldBe "NEW_김모아"
                result.nameLabel shouldBe null
                result.status shouldBe REGISTER_WAITING
            }
        }

        `when`("기존 학생 정보 수정 요청이나, 동일한 학생 정보가 다른 `id` 로 이미 존재하는 경우") {
            val existing = studentFixture.make(name = "업데이트중복", phone = "01000000001", birth = "19900101")
            val savedExisting = studentSaveService.save(existing)

            val target = studentFixture.make(name = "다른이름", phone = "01000000002", birth = "19900202")
            val savedTarget = studentSaveService.save(target)

            val duplicateUpdating = savedTarget.copy(
                name = savedExisting.name,
                phone = savedExisting.phone,
                birth = savedExisting.birth
            )

            val exception = shouldThrow<InvalidRequestException> {
                studentSaveService.save(duplicateUpdating)
            }

            then("'STUDENT_INFO_DUPLICATED' 예외 발생 정상 확인한다 (update)") {
                exception.errorCode shouldBe ErrorCode.STUDENT_INFO_DUPLICATED
            }
        }

        `when`("기존 학생 정보 수정 요청이며, 동일 정보 중복이 아닌 경우") {
            val existing = studentFixture.make(name = "업데이트정상", phone = "01010000000", birth = "19990101")
            val saved = studentSaveService.save(existing)

            val updating = saved.copy(phone = "01020000000")

            val result = studentSaveService.save(updating)

            then("학생 정보 수정 저장 정상 확인한다 (update)") {
                result.id shouldBe saved.id
                result.name shouldBe "업데이트정상"
                result.phone shouldBe "01020000000"
            }
        }

        `when`("기존 학생 정보 수정 요청이며, 이미 동일한 'name' 학생 정보 등록인 경우'") {
            val existingSameName = studentFixture.make(name = "업데이트이름중복", phone = "01030000000", birth = "19930101")
            val savedSameName = studentSaveService.save(existingSameName)

            val target = studentFixture.make(name = "업데이트이름대상", phone = "01040000000", birth = "19940101")
            val savedTarget = studentSaveService.save(target)

            val updatingToSameName = savedTarget.copy(name = savedSameName.name)

            val result = studentSaveService.save(updatingToSameName)

            then("학생 `nameLabel` 정보 '2' 저장 정상 확인한다") {
                result.id shouldBe savedTarget.id
                result.name shouldBe "업데이트이름중복"
                result.nameLabel shouldBe 2
            }
        }
    }

})