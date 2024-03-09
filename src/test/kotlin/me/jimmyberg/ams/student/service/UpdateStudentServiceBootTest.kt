package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.common.domain.Address
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.student.controller.model.StudentModelMapper
import me.jimmyberg.ams.student.domain.School
import me.jimmyberg.ams.student.domain.Student
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UpdateStudentServiceBootTest(
    @Autowired private val saveStudentService: SaveStudentServiceV1,
    @Autowired private val updateStudentService: UpdateStudentServiceV1,
    @Autowired private val mapper: StudentModelMapper
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

    @MethodSource("student")
    @ParameterizedTest
    fun `학생 정보 조회 후 주소 변경하여 업데이트 저장한다`(domain: Student) {
        // given
        val address = Address("12345", "서울시 영등포구 은행로 25", "Hello World")
        val saved = saveStudentService.save(domain)
        val student = saved
            .apply {
                this.zipCode = address.zipCode
                this.baseAddress = address.baseAddress
                this.detailAddress = address.detailAddress
            }

        // when
        val updated = updateStudentService.update(mapper.modelToDomain(student))

        // then
        assertNotNull(updated)
        assertEquals(updated.id, saved.id)
        assertNotNull(updated.address)
    }

}