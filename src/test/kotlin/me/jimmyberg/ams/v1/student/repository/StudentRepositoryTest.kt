package me.jimmyberg.ams.v1.student.repository

import com.example.kotlinmongo.extension.document
import com.example.kotlinmongo.extension.field
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.fixture.StudentDocumentFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["de.flapdoodle.mongodb.embedded.version=5.0.5"])
@DataMongoTest
class StudentRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate
    @Autowired
    lateinit var studentMongoRepository: StudentMongoRepository

    private val studentDocumentFixture = StudentDocumentFixture()

    @Test
    fun `학생 이름 정보 기준 동적 쿼리 생성하여 단건 조회 성공한다`() {
        // given
        val name = "김모건"
        val query = document {
            and(
                { field(StudentDocumentV1::name) eq name }
            )
        }

        studentMongoRepository.save(studentDocumentFixture.make(name = name))

        // when
        val result = mongoTemplate.findOne(query, StudentDocumentV1::class.java)

        // then
        assertThat(result).isNotNull
        assertThat(result!!.school.schoolName).isEqualTo("신길초")
    }

}