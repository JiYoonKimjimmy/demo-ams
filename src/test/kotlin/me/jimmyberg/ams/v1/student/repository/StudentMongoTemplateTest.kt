package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.mongodsl.extension.document
import me.jimmyberg.ams.mongodsl.extension.field
import me.jimmyberg.ams.mongodsl.extension.find
import me.jimmyberg.ams.testsupport.CustomDataMongoTest
import me.jimmyberg.ams.testsupport.CustomStringSpec
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate

@CustomDataMongoTest
class StudentMongoTemplateTest @Autowired constructor(
    private val mongoTemplate: MongoTemplate,
    private val studentMongoRepository: StudentMongoRepository
) : CustomStringSpec({

    val studentDocumentFixture = dependencies.studentDocumentFixture

    beforeTest {
        studentMongoRepository.save(studentDocumentFixture.make(name = "김모건"))
    }

    "학생 이름 정보 기준 동적 쿼리 생성하여 단건 조회 성공한다" {
        // given
        val name = "김모건"
        val query = document {
            and(
                { field(StudentDocument::name) eq name }
            )
        }

        // when
        val result = mongoTemplate.find(query, StudentDocument::class.java).first()

        // then
        assertThat(result).isNotNull
        assertThat(result.name).isEqualTo("김모건")
        assertThat(result.phone).isEqualTo("01012340001")
        assertThat(result.school.schoolName).isEqualTo("신길초")
    }

    "요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 실패 확인한다" {
        // given
        val predicate = StudentPredicate(name = "김모건", phone = "01012345678")
        val query = predicate.query

        // when
        val result = mongoTemplate.find(query, StudentDocument::class).firstOrNull()

        // then
        assertThat(result).isNull()
    }

    "요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 성공 확인한다" {
        // given
        val predicate = StudentPredicate(name = "김모건", phone = "01012340001")
        val query = predicate.query

        // when
        val result = mongoTemplate.find(query, StudentDocument::class).first()

        // then
        assertThat(result).isNotNull
        assertThat(result.name).isEqualTo("김모건")
        assertThat(result.phone).isEqualTo("01012340001")
        assertThat(result.school.schoolName).isEqualTo("신길초")
    }

    "요청 정보 기준 학생 목록 조회 동적 쿼리 생성하여 다건 조회 성공 확인한다" {
        // given
        val pageable = PageRequest.of(0, 1)
        val predicate = StudentPredicate(name = "김모건", phone = "01012340001")
        val query = predicate.query

        // when
        val result = mongoTemplate.find(query, pageable, StudentDocument::class)

        // then
        assertThat(result).isNotEmpty()
        assertThat(result.first().name).isEqualTo("김모건")
        assertThat(result.first().phone).isEqualTo("01012340001")
        assertThat(result.first().school.schoolName).isEqualTo("신길초")
    }

    "요청 정보 기준 학생 목록 조회 동적 쿼리 생성하여 다건 조회 Empty 확인한다" {
        // given
        val pageable = PageRequest.of(9999, 1)
        val predicate = StudentPredicate(name = "김모건", phone = "01012340001")
        val query = predicate.query

        // when
        val result = mongoTemplate.find(query, pageable, StudentDocument::class)

        // then
        assertThat(result).isEmpty()
    }

})