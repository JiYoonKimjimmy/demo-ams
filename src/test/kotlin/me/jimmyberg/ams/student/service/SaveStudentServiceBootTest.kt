package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.student.controller.model.StudentModelMapper
import me.jimmyberg.ams.student.domain.School
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.repository.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SaveStudentServiceBootTest(
//    @Autowired private val saveStudentService: SaveStudentServiceV1
    @Autowired private val studentRepository: StudentRepository,
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
    fun `신규 학생 정보 입력받아 저장한다`(student: Student) {
        // when
        val saved = studentRepository.save(student).let(mapper::domainToModel)

        // then
        assertNotNull(saved)
        assertNotNull(saved.id)
        assertEquals(saved.name, student.name)
    }

}