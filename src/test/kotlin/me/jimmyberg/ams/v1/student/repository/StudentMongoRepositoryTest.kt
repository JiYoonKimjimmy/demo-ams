package me.jimmyberg.ams.v1.student.repository

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.testsupport.annotation.CustomDataMongoTest
import me.jimmyberg.ams.testsupport.CustomStringSpec
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@CustomDataMongoTest
class StudentMongoRepositoryTest(
    private val studentMongoRepository: StudentMongoRepository
) : CustomStringSpec({

    val studentDocumentFixture = dependencies.studentDocumentFixture

    fun save(document: StudentDocument): StudentDocument {
        return studentMongoRepository.save(document)
    }

    fun saveSameNameStudents(name: String) {
        val students = mutableListOf<StudentDocument>()
        for (i in 1..5) {
            students += studentDocumentFixture.make(name = name, indexOfName = i)
        }
        studentMongoRepository.saveAll(students)
    }

    "학생 document 정보 생성하여 저장 성공한다" {
        // given
        val document = studentDocumentFixture.make()

        // when
        val result = studentMongoRepository.save(document)

        // then
        result shouldNotBe null
        result.id shouldNotBe null
    }

    "'id' 기준 학생 정보 조회 성공한다" {
        // given
        val document = save(studentDocumentFixture.make())
        val studentId = document.id

        // when
        val result = studentMongoRepository.findById(studentId!!).get()

        // then
        result shouldNotBe null
        result.id shouldBe studentId
    }

    "'name' 기준 학생 정보 목록 조회 성공한다" {
        // given
        val name = "김모아${LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))}"
        saveSameNameStudents(name)

        // when
        val result = studentMongoRepository.findAllByName(name)

        // then
        result shouldHaveSize 5
    }

    "동일한 'name', 'phone', 'birth' 정보를 가진 학생이 존재하는지 확인하여 'true' 조회 성공한다" {
        // given
        val name = "김모건"
        val phone = "01012341234"
        val birth = "19900309"
        save(studentDocumentFixture.make(name = name, phone = phone, birth = birth))

        // when
        val result = studentMongoRepository.existsByNameAndPhoneAndBirth(name, phone, birth)

        // then
        result shouldBe true
    }

})