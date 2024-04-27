package me.jimmyberg.ams.v0.student.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.service.SaveStudentService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SaveStudentServiceTest {

    private fun studentModel(): StudentModel {
        return StudentModel(
            name = "김모아",
            phone = "01012341234",
            birthday = "19900202",
            gender = Gender.FEMALE,
            schoolName = "여의도중학교",
            schoolType = SchoolType.MIDDLE,
            grade = 1,
            status = StudentStatus.REGISTER_WAITING
        )
    }

    @Test
    fun `학생 정보 신규 생성하여 저장한다`() {
        // given
        val mock = mockk<SaveStudentService>()
        val model = studentModel()
        val domain = StudentMapper().modelToDomain(model)

        every { mock.save(any()) } returns model

        // when
        val result = mock.save(domain)

        // then
        verify(exactly = 1) { mock.save(any()) }
        assertEquals(result.name, domain.name)
    }

}