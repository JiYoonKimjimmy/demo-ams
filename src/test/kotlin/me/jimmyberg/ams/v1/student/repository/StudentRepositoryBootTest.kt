package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.v1.student.document.mongo.StudentDocumentV1
import me.jimmyberg.ams.v1.student.domain.School
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.mongo.StudentMongoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StudentRepositoryBootTest(
    @Autowired private val studentMongoRepository: StudentMongoRepository,
    @Autowired private val studentMapper: StudentMapper
) {
    
    @Test
    fun `Student domain 정보 전달받아 DB 저장한다`() {
        // given
        val student = Student(
            name = "김모아",
            phone = "01012341234",
            birthday = "19900202",
            gender = Gender.FEMALE,
            school = School("여의도중학교", SchoolType.MIDDLE, 1),
            status = StudentStatus.REGISTER_WAITING
        )

        // when
        val saved = saveStudent(student)

        // then
        assertNotNull(saved)
        assertNotNull(saved.id)
        assertEquals(saved.name, student.name)
    }

    private fun saveStudent(domain: Student): StudentDocumentV1 {
        return studentMapper
            .domainToDocumentV1(domain)
            .let(studentMongoRepository::save)
    }
    
}