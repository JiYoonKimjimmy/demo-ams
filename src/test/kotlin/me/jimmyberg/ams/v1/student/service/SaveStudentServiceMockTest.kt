package me.jimmyberg.ams.v1.student.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.domain.School
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepository
import java.util.*

class SaveStudentServiceMockTest: BehaviorSpec({

    val studentRepository = mockk<StudentRepository>()
    val saveStudentService = SaveStudentService(studentRepository, StudentMapper())

    given("학생 정보 저장을 요청하면") {

        val student = Student(
            id = UUID.randomUUID().toString(),
            name = "김모건",
            phone = "01012340001",
            birth = "19900309",
            gender = Gender.MALE,
            school = School("여의도중", SchoolType.MIDDLE, 1),
            status = StudentStatus.REGISTER_WAITING
        )

        `when`("동일한 학생 이름 & 휴대폰번호 & 생년월일 정보가 있는 경우") {
            every { studentRepository.isExistByNameAndPhoneAndBirth(student.name, student.phone, student.birth) } returns true

            then("학생 정보 저장 실패한다") {
                shouldThrow<IllegalArgumentException> { saveStudentService.save(student) }

            }
        }

        `when`("동일한 학생 이름 이미 저장되어 있는 경우") {
            every { studentRepository.isExistByNameAndPhoneAndBirth(student.name, student.phone, student.birth) } returns false
            val duplicatedNameStudent = Student(
                id = UUID.randomUUID().toString(),
                name = "김모건",
                phone = "01012340001",
                birth = "19900309",
                gender = Gender.MALE,
                school = School("여의도중", SchoolType.MIDDLE, 1),
                status = StudentStatus.REGISTER_WAITING
            )
            every { studentRepository.findAllByName(student.name) } returns listOf(duplicatedNameStudent)
            every { studentRepository.save(student) } returns student

            val saved = saveStudentService.save(student)

            then("학생 이름 인덱스를 증가시킨 후, 정상 저장한다") {
                saved.name shouldBe "${student.name}2"
            }
        }

        `when`("중복없이 신규 학생 정보인 경우") {
            every { studentRepository.isExistByNameAndPhoneAndBirth(student.name, student.phone, student.birth) } returns false
            every { studentRepository.findAllByName(student.name) } returns emptyList()
            every { studentRepository.save(student) } returns student

            val saved = saveStudentService.save(student)

            then("학생 정보를 정상 저장한다") {
                saved.id shouldNotBe null
                saved.name shouldBe student.name
            }
        }

    }

})