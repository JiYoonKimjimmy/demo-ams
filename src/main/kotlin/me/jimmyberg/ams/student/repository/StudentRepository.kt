package me.jimmyberg.ams.student.repository

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.domain.StudentMapper
import me.jimmyberg.ams.student.repository.mongo.StudentMongoRepository
import org.springframework.stereotype.Repository

@Repository
class StudentRepository(
    private val studentMongoRepository: StudentMongoRepository,
    private val studentMapper: StudentMapper
) {

    private fun convertDocumentToDomain(document: StudentDocumentV1): Student {
        return studentMapper.documentToDomain(document)
    }

    fun findById(id: String): Student {
        return studentMongoRepository
            .findById(id)
            .orElseThrow()
            .let(this::convertDocumentToDomain)
    }

    fun findAll(): List<Student> {
        return studentMongoRepository
            .findAll()
            .map(this::convertDocumentToDomain)
    }

    fun save(domain: Student): Student {
        return studentMapper
            .domainToDocumentV1(domain)
            .let(studentMongoRepository::save)
            .let(this::convertDocumentToDomain)
    }

}