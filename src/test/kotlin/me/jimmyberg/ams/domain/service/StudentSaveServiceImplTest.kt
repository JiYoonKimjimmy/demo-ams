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

    given("신규 학생 정보 저장 요청되어") {
        fun saveStudent(name: String, phone: String, birth: String) =
            studentSaveService.save(studentFixture.make(name = name, phone = phone, birth = birth))

        val duplicateName = "김중복"
        val baseName = "김모건"
        val newName = "NEW_김모아"
        val phoneDefault = "01012340001"
        val phoneOther = "01012341234"
        val birthDefault = "19900309"

        `when`("신규 학생 정보 저장 요청이나, 동일한 학생 정보 이미 등록되어 있는 경우") {
            // given: 동일한 학생 정보가 이미 저장됨
            saveStudent(duplicateName, phoneDefault, birthDefault)

            // when: 동일한 정보로 재저장 요청
            val duplicateStudent = studentFixture.make(name = duplicateName, phone = phoneDefault, birth = birthDefault)
            val exception = shouldThrow<InvalidRequestException> {
                studentSaveService.save(duplicateStudent)
            }

            then("'STUDENT_INFO_DUPLICATED' 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.STUDENT_INFO_DUPLICATED
            }
        }

        `when`("이미 동일한 'name' 학생 정보 등록인 경우'") {
            // given: 동일한 이름의 학생이 존재
            saveStudent(baseName, phoneDefault, birthDefault)

            // when: 동일 이름으로 신규 저장(중복 아님)
            val result = saveStudent(baseName, phoneOther, birthDefault)

            then("학생 `nameLabel` 정보 '2' 저장 정상 확인한다") {
                result.name shouldBe "김모건"
                result.nameLabel shouldBe 2
            }
        }

        `when`("신규 학생 정보 등록인 경우") {
            val result = saveStudent(newName, phoneOther, birthDefault)

            then("학생 정보를 저장 정상 확인한다") {
                result.name shouldBe "NEW_김모아"
                result.nameLabel shouldBe null
                result.status shouldBe REGISTER_WAITING
            }
        }
    }

    given("기존 학생 정보 수정 요청되어") {
        fun saveStudent(name: String, phone: String, birth: String) =
            studentSaveService.save(studentFixture.make(name = name, phone = phone, birth = birth))

        fun updateAndSave(
            base: me.jimmyberg.ams.domain.model.Student,
            name: String? = null,
            phone: String? = null,
            birth: String? = null
        ) = studentSaveService.save(
            base.copy(
                name = name ?: base.name,
                phone = phone ?: base.phone,
                birth = birth ?: base.birth
            )
        )

        val duplicateName = "중복이름"
        val newName = "새로운이름"

        `when`("기존 학생 정보 수정 요청이나, 동일한 학생 정보가 다른 `id` 로 이미 존재하는 경우") {
            val savedExisting = saveStudent(duplicateName, "01000000001", "19900101")
            val savedTarget = saveStudent(newName, "01000000002", "19900202")

            val exception = shouldThrow<InvalidRequestException> {
                updateAndSave(
                    base = savedTarget,
                    name = savedExisting.name,
                    phone = savedExisting.phone,
                    birth = savedExisting.birth
                )
            }

            then("'STUDENT_INFO_DUPLICATED' 예외 발생 정상 확인한다 (update)") {
                exception.errorCode shouldBe ErrorCode.STUDENT_INFO_DUPLICATED
            }
        }

        `when`("기존 학생 정보 수정 요청이며, 동일 정보 중복이 아닌 경우") {
            val saved = saveStudent(newName, "01010000000", "19990101")
            val result = updateAndSave(saved, phone = "01020000000")

            then("학생 정보 수정 저장 정상 확인한다 (update)") {
                result.id shouldBe saved.id
                result.name shouldBe "새로운이름"
                result.phone shouldBe "01020000000"
            }
        }

        `when`("기존 학생 정보 수정 요청이며, 이미 동일한 'name' 학생 정보 등록인 경우'") {
            val savedSameName = saveStudent("업데이트이름", "01030000000", "19930101")
            val savedTarget = saveStudent(newName, "01040000000", "19940101")

            val result = updateAndSave(savedTarget, name = savedSameName.name)

            then("학생 `nameLabel` 정보 '2' 저장 정상 확인한다") {
                result.id shouldBe savedTarget.id
                result.name shouldBe "업데이트이름"
                result.nameLabel shouldBe 2
            }
        }
    }

})