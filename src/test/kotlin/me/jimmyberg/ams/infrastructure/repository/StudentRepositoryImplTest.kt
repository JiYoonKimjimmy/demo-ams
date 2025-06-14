package me.jimmyberg.ams.infrastructure.repository

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.presentation.common.PageableRequest
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import org.jetbrains.exposed.sql.transactions.transaction

class StudentRepositoryImplTest : CustomBehaviorSpec({

    listeners(H2DatasourceTestListener)

    val studentRepository = dependencies.studentRepository
    val studentFixture = dependencies.studentFixture

    lateinit var saved: Student

    beforeSpec {
        transaction { saved = studentRepository.save(studentFixture.make()) }
    }

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
            val result =
                transaction { studentRepository.isExistByNameAndPhoneAndBirth("김모긴", "01012341235", "19900310") }

            then("'false' 결과 정상 확인한다") {
                result shouldBe false
            }
        }

        `when`("이미 있는 경우") {
            val result =
                transaction { studentRepository.isExistByNameAndPhoneAndBirth(saved.name, saved.phone, saved.birth) }

            then("'true' 결과 정상 확인한다") {
                result shouldBe true
            }
        }
    }

    given("Student 단건 조회 요청하여") {

        `when`("조회 조건 정보 없는 경우") {
            val result = transaction { studentRepository.findByPredicate(StudentPredicate()) }

            then("DB 조회 결과 'null' 정상 확인한다") {
                result shouldBe null
            }
        }

        `when`("'name', 'phone', 'birth' 조회 조건 추가인 경우") {
            val result = transaction {
                studentRepository.findByPredicate(
                    StudentPredicate(
                        name = saved.name,
                        phone = saved.phone,
                        birth = saved.birth
                    )
                )
            }

            then("DB 조회 결과 정상 확인한다") {
                result!! shouldNotBe null
                result.id shouldBe saved.id
                result.name shouldBe saved.name
                result.phone shouldBe saved.phone
                result.birth shouldBe saved.birth
            }
        }
    }

    given("Student 'name', 'phone', 'birth' 조회 조건 다건 조회 요청하여") {
        val predicate = StudentPredicate(name = saved.name, phone = saved.phone, birth = saved.birth)

        `when`("조회 결과 성공인 경우") {
            val result = transaction { studentRepository.findAllByPredicate(predicate) }

            then("DB 조회 결과 '1'건 정상 확인한다") {
                result.shouldNotBeEmpty()
                result.first().id shouldBe saved.id
            }
        }
    }

    given("Student 'size: 1' scroll 조회 요청하여") {
        val predicate = StudentPredicate()
        val pageable = PageableRequest(size = 1)

        `when`("'1'건 조회 결과 성공인 경우") {
            val result = transaction { studentRepository.scrollByPredicate(predicate, pageable) }

            then("'content', 'hasNext : false' DB 조 결과 정상 확인한다") {
                result.content.shouldNotBeEmpty()
            }
        }
    }

})