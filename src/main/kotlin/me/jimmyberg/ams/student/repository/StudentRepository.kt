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

    fun findAll(): List<Student> {
        return studentMongoRepository
            .findAll()
            .map(studentMapper::documentToDomain)
    }

    fun save(domain: Student): Student {
        return studentMapper
            .domainToDocumentV1(domain)
            .let(studentMongoRepository::save)
            .let(studentMapper::documentToDomain)
    }

}