package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.fixture.StudentDocumentFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["de.flapdoodle.mongodb.embedded.version=5.0.5"])
@DataMongoTest
class StudentMongoRepositoryTest(
    @Autowired private val studentMongoRepository: StudentMongoRepository
) {

    private val studentDocumentFixture = StudentDocumentFixture()

    @Test
    fun `학생 document 정보 생성하여 저장 성공한다`() {
        // given
        val document = studentDocumentFixture.make()

        // when
        val result = studentMongoRepository.save(document)

        // then
        assertThat(result.id).isNotNull
    }

    @Test
    fun `'id' 기준 학생 정보 조회 성공한다`() {
        // given
        val document = studentDocumentFixture.make().let { studentMongoRepository.save(it) }
        val studentId = document.id

        // when
        val result = studentMongoRepository.findById(studentId!!).get()

        // then
        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(studentId)
    }

    @Test
    fun `'name' 기준 학생 정보 목록 조회 성공한다`() {
        // given
        saveSameNameStudents()
        val name = "김모건"

        // when
        val result = studentMongoRepository.findAllByName(name)

        // then
        assertThat(result).hasSize(5)
    }

    private fun saveSameNameStudents() {
        val students = mutableListOf<StudentDocumentV1>()
        for (i in 1..5) {
            students += studentDocumentFixture.make(name = "김모건", indexOfName = i)
        }
        studentMongoRepository.saveAll(students)
    }

    @Test
    fun `동일한 'name', 'phone', 'birth' 정보를 가진 학생이 존재하는지 확인하여 'true' 조회 성공한다`() {
        // given
        val name = "김모건"
        val phone = "01012341234"
        val birth = "19900309"
        studentDocumentFixture.make(name = name, phone = phone, birth = birth).let { studentMongoRepository.save(it) }

        // when
        val result = studentMongoRepository.existsByNameAndPhoneAndBirth(name, phone, birth)

        // then
        assertThat(result).isTrue()
    }

}