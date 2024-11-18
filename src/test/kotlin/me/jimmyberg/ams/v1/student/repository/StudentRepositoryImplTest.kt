package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.testsupport.CustomBehaviorSpec
import me.jimmyberg.ams.testsupport.CustomDataMongoTest
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.data.mongodb.core.MongoTemplate

@CustomDataMongoTest
class StudentRepositoryImplTest(
    private val studentMongoRepository: StudentMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture
    val studentRepository = StudentRepositoryImpl(StudentMapper(), studentMongoRepository, mongoTemplate)

    fun saveStudentDocument(vararg students: Student) {
        students.forEach(studentRepository::save)
    }

    given("StudentDocument 정보 생성하여") {
        val document = studentFixture.make()

        `when`("정상 생성 성공인 경우") {
            val result = studentRepository.save(document)

            then("DB 저장 처리 정상 확인한다") {
                result shouldNotBe null
                result.id shouldNotBe null
            }
        }
    }

    given("StudentDocument Scroll 다건 조회하여") {
        val predicate = StudentPredicate()
        val pageable = PageableRequest(size = 1)

        `when`("Document 비어있는 경우") {
            val result = studentRepository.scrollByPredicate(predicate, pageable)

            then("DB 조회 결과 정상 확인한다") {
                result.size shouldBe 0
                result.hasNext shouldBe false
                result.isLast shouldBe true
                result.isEmpty shouldBe true
            }
        }

        // 학생 정보 1건 저장
        saveStudentDocument(studentFixture.make())

        `when`("Document '1'건 저장되어 있는 경우") {
            val result = studentRepository.scrollByPredicate(predicate, pageable)

            then("DB 조회 결과 정상 확인한다") {
                result.size shouldBe 1
                result.hasNext shouldBe false
                result.isLast shouldBe true
                result.isEmpty shouldBe false
            }
        }

        // 학생 정보 1건 저장
        saveStudentDocument(studentFixture.make())

        `when`("Document '2'건 저장되어 있는 경우") {
            val result = studentRepository.scrollByPredicate(predicate, pageable)

            then("DB 조회 결과 정상 확인한다") {
                result.size shouldBe 1
                result.hasNext shouldBe true
                result.isLast shouldBe false
                result.isEmpty shouldBe false
            }
        }
    }

})