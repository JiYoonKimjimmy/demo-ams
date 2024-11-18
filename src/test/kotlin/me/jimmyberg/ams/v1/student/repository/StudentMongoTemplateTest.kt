package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.mongodsl.extension.*
import me.jimmyberg.ams.testsupport.annotation.CustomDataMongoTest
import me.jimmyberg.ams.testsupport.CustomStringSpec
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate

@CustomDataMongoTest
class StudentMongoTemplateTest(
    private val mongoTemplate: MongoTemplate,
    private val studentMongoRepository: StudentMongoRepository
) : CustomStringSpec({

    val studentDocumentFixture = dependencies.studentDocumentFixture
    lateinit var saved: StudentDocument

    beforeSpec {
        studentMongoRepository.deleteAll()
        saved = studentMongoRepository.save(studentDocumentFixture.make(name = "김모건"))
    }

    "학생 이름 정보 기준 동적 쿼리 생성하여 단건 조회 성공한다" {
        // given
        val name = saved.name
        val query = document {
            and(
                { field(StudentDocument::name) eq name }
            )
        }

        // when
        val result = mongoTemplate.find(query, StudentDocument::class.java).first()

        // then
        result shouldNotBe null
        result.name shouldBe saved.name
        result.phone shouldBe saved.phone
        result.school.schoolName shouldBe saved.school.schoolName
    }

    "요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 실패 확인한다" {
        // given
        val predicate = StudentPredicate(name = saved.name, phone = "01012345678")
        val query = predicate.query

        // when
        val result = mongoTemplate.findOne(query, StudentDocument::class)

        // then
        result shouldBe null
    }

    "요청 정보 기준 학생 조회 동적 쿼리 생성하여 단건 조회 성공 확인한다" {
        // given
        val predicate = StudentPredicate(name = saved.name, phone = saved.phone)
        val query = predicate.query

        // when
        val result = mongoTemplate.findOne(query, StudentDocument::class)!!

        // then
        result shouldNotBe null
        result.name shouldBe saved.name
        result.phone shouldBe saved.phone
        result.school.schoolName shouldBe saved.school.schoolName
    }

    "요청 정보 기준 학생 목록 조회 동적 쿼리 생성하여 다건 조회 성공 확인한다" {
        // given
        val pageable = PageRequest.of(0, 1)
        val predicate = StudentPredicate(name = saved.name, phone = saved.phone)
        val query = predicate.query

        // when
        val result = mongoTemplate.findAll(query, pageable, StudentDocument::class)

        // then
        result.isNotEmpty() shouldBe true

        val student = result.first()
        student.name shouldBe saved.name
        student.phone shouldBe saved.phone
        student.school.schoolName shouldBe saved.school.schoolName
    }

    "요청 정보 기준 학생 목록 조회 동적 쿼리 생성하여 다건 조회 Empty 확인한다" {
        // given
        val pageable = PageRequest.of(9999, 1)
        val predicate = StudentPredicate(name = saved.name, phone = saved.phone)
        val query = predicate.query

        // when
        val result = mongoTemplate.findAll(query, pageable, StudentDocument::class)

        // then
        result.isEmpty() shouldBe true
    }

    "요청 정보 기준 학생 목록 조회하여 'isEmpty: false, isLast: true, hasNext: false' 정상 확인한다" {
        // given
        val pageable = PageRequest.of(0, 1)
        val predicate = StudentPredicate(name = saved.name, phone = saved.phone)
        val query = predicate.query

        // when
        val result = mongoTemplate.scroll(query, pageable, StudentDocument::class)

        // then
        result.isEmpty shouldBe false
        result.isLast shouldBe true
        result.hasNext() shouldBe false
        result.content.first().id shouldBe saved.id
    }

})