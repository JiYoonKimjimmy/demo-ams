package me.jimmyberg.ams.student.repository.mongo

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import me.jimmyberg.ams.student.domain.Gender
import me.jimmyberg.ams.student.domain.SchoolType
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
    fun `신규 학생 정보를 생성하고 DB 저장한다`() {
        // given
        val document = StudentDocumentV1(
            name = "김모건",
            phone = "",
            birthday = "19900309",
            gender = Gender.MALE,
            schoolName = "신길초",
            schoolType = SchoolType.PRIMARY,
            grade = 1
        )

        // when
        val saved = studentMongoRepository.save(document)

        // then
        val data = studentMongoRepository.findAll()
        assertNotNull(data)
        assertEquals(data.last().id, saved.id)
        assertEquals(data.last().name, document.name)
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