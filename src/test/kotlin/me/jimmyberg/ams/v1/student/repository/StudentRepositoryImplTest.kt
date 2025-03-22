package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import me.jimmyberg.ams.testsupport.kotest.listener.H2DatasourceTestListener
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
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

    given("Student 단건 조회 요청하여") {

        `when`("조회 조건 정보 없는 경우") {
            val result = transaction { studentRepository.findByPredicate(StudentPredicate()) }

            then("DB 조회 결과 'null' 정상 확인한다") {
                result shouldBe null
            }
        }

        `when`("'name', 'phone', 'birth' 조회 조건 추가인 경우") {
            val result = transaction { studentRepository.findByPredicate(StudentPredicate(name = saved.name, phone = saved.phone, birth = saved.birth)) }

            then("DB 조회 결과 정상 확인한다") {
                result!! shouldNotBe null
                result.id shouldBe saved.id
                result.name shouldBe saved.name
                result.phone shouldBe saved.phone
                result.birth shouldBe saved.birth
            }
        }

    }

})