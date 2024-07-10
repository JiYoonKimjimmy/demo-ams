package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.mongodsl.extension.document
import me.jimmyberg.ams.mongodsl.extension.field
import me.jimmyberg.ams.testcode.CustomDataMongoTest
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.fixture.StudentDocumentFixture
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

@CustomDataMongoTest
class StudentMongoTemplateTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate
    @Autowired
    lateinit var studentMongoRepository: StudentMongoRepository

    private val studentDocumentFixture = StudentDocumentFixture()

    @BeforeEach
    fun setup() {
        studentMongoRepository.save(studentDocumentFixture.make(name = "김모건"))
    }

    @Test
    fun `학생 이름 정보 기준 동적 쿼리 생성하여 단건 조회 성공한다`() {
        // given
        val name = "김모건"
        val query = document {
            and(
                { field(StudentDocumentV1::name) eq name }
            )
        }

        // when
        val result = mongoTemplate.findOne(query, StudentDocumentV1::class.java)

        // then
        assertThat(result!!).isNotNull
        assertThat(result.name).isEqualTo("김모건")
        assertThat(result.phone).isEqualTo("01012340001")
        assertThat(result.school.schoolName).isEqualTo("신길초")
    }

    @Test
    fun `요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 실패 확인한다`() {
        // given
        val predicate = StudentPredicate(name = "김모건", "01012345678")
        val query = predicate.query

        // when
        val result = mongoTemplate.findOne(query, StudentDocumentV1::class.java)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 성공 확인한다`() {
        // given
        val predicate = StudentPredicate(name = "김모건", "01012340001")
        val query = predicate.query

        // when
        val result = mongoTemplate.findOne(query, StudentDocumentV1::class.java)

        // then
        assertThat(result!!).isNotNull
        assertThat(result.name).isEqualTo("김모건")
        assertThat(result.phone).isEqualTo("01012340001")
        assertThat(result.school.schoolName).isEqualTo("신길초")
    }

}