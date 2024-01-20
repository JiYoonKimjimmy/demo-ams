package me.jimmyberg.ams.student.service

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FindStudentServiceBootTest(
    @Autowired private val findStudentService: FindStudentService
) {

    @Test
    fun `학생 정보 전제 조회 성공한다`() {
        // when
        val students = findStudentService.findAll()

        // then
        assertNotNull(students)
        assertTrue(students.isNotEmpty())
    }

}