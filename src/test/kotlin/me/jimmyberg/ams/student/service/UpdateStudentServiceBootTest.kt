package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.domain.School
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.domain.StudentMapper
import me.jimmyberg.ams.student.repository.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UpdateStudentServiceBootTest(
    @Autowired private val studentRepository: StudentRepository,
    @Autowired private val mapper: StudentMapper
) {

    companion object {
        @JvmStatic
        fun student(): Set<Student> {
            val student = Student(
                name = "김모아",
                phone = "01012341234",
                birthday = "19900202",
                gender = Gender.FEMALE,
                school = School(
                    schoolName = "여의도중학교",
                    schoolType = SchoolType.MIDDLE,
                    grade = 1
                ),
                status = StudentStatus.REGISTER_WAITING
            )
            return setOf(student)
        }
    }

    private fun saveStudent(student: Student): StudentModel {
        return studentRepository.save(student).let(mapper::domainToModel)
    }

    private fun updateAddress(student: StudentModel): StudentModel {
        return student
            .apply {
                this.zipCode = "12345"
                this.baseAddress = "서울시 강서구 허준로"
                this.detailAddress = "Hello World"
            }
    }

    @MethodSource("student")
    @ParameterizedTest
    fun `학생 정보 조회 후 주소 변경하여 업데이트 저장한다`(student: Student) {
        // given
        val saved = saveStudent(student).let(this::updateAddress)
        val domain = saved.let(this::updateAddress).let(mapper::modelToDomain)

        // when
        val updated = studentRepository.save(domain).let(mapper::domainToModel)

        // then
        assertNotNull(updated)
        assertEquals(updated.id, saved.id)
        assertNotNull(updated.address)
    }

}