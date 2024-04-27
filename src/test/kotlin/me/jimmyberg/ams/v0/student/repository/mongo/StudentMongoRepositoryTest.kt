package me.jimmyberg.ams.v0.student.repository.mongo

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.v1.student.document.mongo.StudentDocumentV1
import me.jimmyberg.ams.v1.student.domain.School
import me.jimmyberg.ams.v1.student.repository.mongo.StudentMongoRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class StudentMongoRepositoryTest(
    @Autowired private val studentMongoRepository: StudentMongoRepository
) {

    private lateinit var studentDocument: StudentDocumentV1

    @BeforeEach
    fun before() {
        val document = StudentDocumentV1(
            name = "김모건",
            phone = "01012341234",
            birthday = "19900309",
            gender = Gender.MALE,
            school = School("신길초", SchoolType.PRIMARY, 1)
        )
        studentDocument = studentMongoRepository.save(document)
    }

    @AfterEach
    fun after() {
        studentMongoRepository.delete(studentDocument)
    }

    @Test
    fun `학생 이름이 '김모건' 데이터를 조회한다`() {
        // given
        val name = "김모건"

        // when
        val document = studentMongoRepository.findByName(name)

        // then
        assertNotNull(document)
        assertEquals(document?.name, name)
    }

}