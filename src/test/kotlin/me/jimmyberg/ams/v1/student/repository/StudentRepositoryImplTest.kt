package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.jetbrains.exposed.sql.transactions.transaction

class StudentRepositoryImplTest : CustomBehaviorSpec({

    listeners(H2DatasourceTestListener)

    val studentRepository = dependencies.studentRepository
    val studentFixture = dependencies.studentFixture

    lateinit var saved: Student

    beforeTest {
        transaction { saved = studentRepository.save(studentFixture.make()) }
    }

//    fun saveStudentDocument(vararg students: Student) {
//        students.forEach(studentRepository::save)
//    }

    given("Student DB 정보 생성 요청하여") {
        val student = studentFixture.make()

        `when`("성공인 경우") {
            val result = transaction { studentRepository.save(student) }

            then("처리 결과 정상 확인한다") {
                result shouldNotBe null
                result.id shouldNotBe null
            }
        }
    }

    given("Student name, phone, birth 기준 저장 여부 확인 요청하여") {

        `when`("없는 경우") {
            val result = transaction { studentRepository.isExistByNameAndPhoneAndBirth("김모긴", "01012341235", "19900310") }

            then("'false' 결과 정상 확인한다") {
                result shouldBe false
            }
        }

        `when`("이미 있는 경우") {
            val result = transaction { studentRepository.isExistByNameAndPhoneAndBirth(saved.name, saved.phone, saved.birth) }

            then("'true' 결과 정상 확인한다") {
                result shouldBe true
            }
        }
    }

//    given("StudentDocument Scroll 다건 조회하여") {
//        val predicate = StudentPredicate()
//        val pageable = PageableRequest(size = 1)
//
//        `when`("Document 비어있는 경우") {
//            val result = studentRepository.scrollByPredicate(predicate, pageable)
//
//            then("DB 조회 결과 정상 확인한다") {
//                result.size shouldBe 0
//                result.hasNext shouldBe false
//                result.isLast shouldBe true
//                result.isEmpty shouldBe true
//            }
//        }
//
//        // 학생 정보 1건 저장
//        saveStudentDocument(studentFixture.make())
//
//        `when`("Document '1'건 저장되어 있는 경우") {
//            val result = studentRepository.scrollByPredicate(predicate, pageable)
//
//            then("DB 조회 결과 정상 확인한다") {
//                result.size shouldBe 1
//                result.hasNext shouldBe false
//                result.isLast shouldBe true
//                result.isEmpty shouldBe false
//            }
//        }
//
//        // 학생 정보 1건 저장
//        saveStudentDocument(studentFixture.make(), studentFixture.make())
//
//        `when`("Document '2'건 저장되어 있는 경우") {
//            val result = studentRepository.scrollByPredicate(predicate, pageable)
//
//            then("DB 조회 결과 정상 확인한다") {
//                result.size shouldBe 1
//                result.hasNext shouldBe true
//                result.isLast shouldBe false
//                result.isEmpty shouldBe false
//            }
//        }
//    }

})