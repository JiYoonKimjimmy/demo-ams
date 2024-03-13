package me.jimmyberg.ams.student.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.controller.model.StudentModelMapper
import me.jimmyberg.ams.student.domain.School
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.repository.StudentRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

class SaveStudentServiceTest {

    @Test
    fun `학생 정보 신규 생성하여 저장한다`() {
        // given
        val domain = Student(
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
        val model = StudentModel(
            name = "김모아",
            phone = "01012341234",
            birthday = "19900202",
            gender = Gender.FEMALE,
            schoolName = "여의도중학교",
            schoolType = SchoolType.MIDDLE,
            grade = 1,
            status = StudentStatus.REGISTER_WAITING
        )
        val mock = mockk<SaveStudentService>()

        every { mock.save(any()) } returns model

        // when
        val result = mock.save(domain)

        // then
        verify(exactly = 1) { mock.save(any()) }
        assertEquals(result.name, domain.name)
    }

}