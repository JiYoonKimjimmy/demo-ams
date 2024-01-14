package me.jimmyberg.ams.student.controller

import me.jimmyberg.ams.student.service.FindStudentService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FindStudentControllerBootTest(
    @Autowired private val findStudentService: FindStudentService
) {

    @Test
    fun `학생 정보 전제 조회한다`() {
        // when
        val students = findStudentService.findAll()

        // then
        assertNotNull(students)
        assertTrue(students.isNotEmpty())
    }

}