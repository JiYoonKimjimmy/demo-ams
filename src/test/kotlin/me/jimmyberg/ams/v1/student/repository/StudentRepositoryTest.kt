package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.fixture.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.repository.mongo.StudentMongoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import

@AutoConfigureDataMongo
@Import(StudentRepository::class)
@DataMongoTest
class StudentRepositoryTest {

    @Autowired
    lateinit var studentRepository: StudentRepositoryV1
    private val studentDocumentFixture = StudentDocumentFixture()

    companion object {
        @JvmStatic
        @AfterAll
        fun afterAll(@Autowired studentMongoRepository: StudentMongoRepository) {
            studentMongoRepository.deleteAll()
        }
    }

    @Test
    fun `학생 document 정보 생성하여 저장 성공한다`() {
        // given
        val document = studentDocumentFixture.make()

        // when
        val result = studentRepository.save(document)

        // then
        assertThat(result.id).isNotNull()
    }

}