package me.jimmyberg.ams.student.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FindStudentServiceBootTest(
    @Autowired private val findStudentService: FindStudentService
) {

    @Test
    fun `학생 단건 조회 성공한다`() {
        // given
        val studentId = "65a3a122d71a357c46559977"

        // when
        val student = findStudentService.findOne(studentId)

        // then
        println(student)
        assertNotNull(student)
        assertEquals(student.id, studentId)
        assertEquals(student.name, "김모건")
    }

    @Test
    fun `학생 정보 전제 조회 성공한다`() {
        // when
        val students = findStudentService.findAll()

        // then
        assertNotNull(students)
        assertTrue(students.isNotEmpty())
    }

}