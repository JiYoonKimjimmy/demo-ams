package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.document.mongo.StudentDocumentV1
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.mongo.StudentMongoRepository
import org.springframework.stereotype.Repository

@Repository
class StudentRepository(
    private val studentMongoRepository: StudentMongoRepository,
    private val studentMapper: StudentMapper
) {

    private fun convertDomainToDocument(domain: Student): StudentDocumentV1 {
        return studentMapper.domainToDocumentV1(domain)
    }

    private fun convertDocumentToDomain(document: StudentDocumentV1): Student {
        return studentMapper.documentToDomain(document)
    }

    fun save(domain: Student): Student {
        return convertDomainToDocument(domain)
            .let(studentMongoRepository::save)
            .let(this::convertDocumentToDomain)
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

}