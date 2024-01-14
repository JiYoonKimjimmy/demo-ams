package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.controller.model.SaveStudentRequest
import me.jimmyberg.ams.student.domain.Gender
import me.jimmyberg.ams.student.domain.SchoolType
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.service.SaveStudentService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SaveStudentControllerBootTest(
    @Autowired private val saveStudentService: SaveStudentService
) {

    companion object {
        @JvmStatic
        fun saveStudentRequest(): Set<SaveStudentRequest> {
            return SaveStudentRequest(
                name = "김모건",
                phone = "01012341234",
                birthday = "19900309",
                gender = Gender.MALE,
                schoolName = "신길초등학교",
                schoolType = SchoolType.PRIMARY,
                grade = 1
            ).let { setOf(it) }
        }
    }

    @MethodSource("saveStudentRequest")
    @ParameterizedTest
    fun `신규 학생 정보 입력받아 저장한다`(request: SaveStudentRequest) {
        // given
        val domain = Student(
            name = request.name,
            phone = request.phone,
            birthday = request.birthday,
            gender = request.gender,
            address = request.address,
            schoolName = request.schoolName,
            schoolType = request.schoolType,
            grade = request.grade,
            status = request.status
        )

        // when
        val saved = saveStudentService.save(domain)

        // then
        assertNotNull(saved)
        assertEquals(saved.name, request.name)
    }

}