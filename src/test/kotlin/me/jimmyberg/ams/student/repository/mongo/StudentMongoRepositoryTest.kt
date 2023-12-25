package me.jimmyberg.ams.student.repository.mongo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class StudentMongoRepositoryTest(
    @Autowired private val studentMongoRepository: StudentMongoRepository
) {

    @Test
    fun `'student' document 전체 조회한다`() {
        // when
        val data = studentMongoRepository.findAll()

        // then
        assertNotNull(data)
        assertEquals(data.size, 2)
    }

}