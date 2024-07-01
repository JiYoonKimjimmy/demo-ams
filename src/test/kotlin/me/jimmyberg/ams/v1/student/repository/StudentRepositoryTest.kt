package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.fixture.StudentDocumentFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["de.flapdoodle.mongodb.embedded.version=5.0.5"])
@DataMongoTest
class StudentRepositoryTest(
    @Autowired private val studentMongoRepository: StudentMongoRepository
) {

    private val studentDocumentFixture = StudentDocumentFixture()

    @Test
    fun `학생 document 정보 생성하여 저장 성공한다`() {
        // given
        val document = studentDocumentFixture.make()

        // when
        val result = studentMongoRepository.save(document)

        // then
        assertThat(result.id).isNotNull
    }

    @Test
    fun `'id' 기준 학생 정보 조회 성공한다`() {
        // given
        val document = studentDocumentFixture.make().let { studentMongoRepository.save(it) }
        val studentId = document.id

        // when
        val result = studentMongoRepository.findById(studentId!!).get()

        // then
        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(studentId)
    }

}