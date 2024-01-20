package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.student.controller.model.SaveStudentRequest
import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.domain.Student
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SaveStudentServiceBootTest(
    @Autowired private val saveStudentService: SaveStudentServiceV1
) {

    companion object {
        @JvmStatic
        fun saveStudentRequest(): Set<SaveStudentRequest> {
            val student = StudentModel(
                name = "김모아",
                phone = "01012341234",
                birthday = "19900202",
                gender = Gender.FEMALE,
                schoolName = "여의도중학교",
                schoolType = SchoolType.MIDDLE,
                grade = 1
            )
            return setOf(SaveStudentRequest(student))
        }
    }

    @MethodSource("saveStudentRequest")
    @ParameterizedTest
    fun `신규 학생 정보 입력받아 저장한다`(request: SaveStudentRequest) {
        // given
        val domain = Student(
            name = request.student.name,
            phone = request.student.phone,
            birthday = request.student.birthday,
            gender = request.student.gender,
            address = request.student.address,
            schoolName = request.student.schoolName,
            schoolType = request.student.schoolType,
            grade = request.student.grade,
            status = request.student.status
        )

        // when
        val saved = saveStudentService.save(domain)

        // then
        assertNotNull(saved)
        assertEquals(saved.name, request.student.name)
    }

}