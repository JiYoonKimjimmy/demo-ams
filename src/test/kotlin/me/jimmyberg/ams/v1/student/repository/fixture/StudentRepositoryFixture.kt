package me.jimmyberg.ams.v1.student.repository.fixture

import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import java.util.*

class StudentRepositoryFixture {

    private val studentMapper = StudentMapper()
    private val students = StudentDocumentFixture.documents

    fun findAllStudentByName(name: String): List<Student> {
        return students.filter { it.name == name }.map { studentMapper.documentToDomain(it) }
    }

    fun findStudentByNameAndSchoolName(name: String, schoolName: String): Optional<Student> {
        val result = students.filter { it.name == name && it.school.schoolName == schoolName }
        if (result.size > 1) {
            throw IllegalArgumentException()
        }
        if (result.isEmpty()) {
            throw NotFoundException()
        }
        return result.first().let { studentMapper.documentToDomain(it) }.let { Optional.of(it) }
    }

    fun existStudentByNameAndPhoneAndBirth(name: String, phone: String, birth: String) {
        students
            .any { it.name == name && it.phone == phone && it.birth == birth }
            .takeIf { it }
            .let { throw IllegalArgumentException() }
    }

    fun saveStudent(student: Student): Student {
        val document = studentMapper.domainToDocumentV1(student)
        document.apply { id = UUID.randomUUID().toString() }
        students.add(document)
        return studentMapper.documentToDomain(document)
    }

}