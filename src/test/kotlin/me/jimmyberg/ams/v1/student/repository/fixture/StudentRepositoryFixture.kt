package me.jimmyberg.ams.v1.student.repository.fixture

import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import java.util.*

class StudentRepositoryFixture {

    private val studentMapper = StudentMapper()
    private val students = StudentDocumentFixture.documents

    fun findStudentByName(name: String, schoolName: String): Optional<Student> {
        val result = students.filter { it.name == name && it.school.schoolName == schoolName }
        if (result.size > 1) {
            throw IllegalArgumentException()
        }
        if (result.isEmpty()) {
            throw NotFoundException()
        }
        return result.first().let { studentMapper.documentToDomain(it) }.let { Optional.of(it) }
    }

    fun saveStudent(student: Student): Student {
        val document = studentMapper.domainToDocumentV1(student)
        document.apply { id = UUID.randomUUID().toString() }
        students.add(document)
        return studentMapper.documentToDomain(document)
    }

}