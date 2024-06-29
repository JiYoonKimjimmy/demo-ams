package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.mongo.StudentMongoRepository
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val studentMongoRepository: StudentMongoRepository,
    private val mapper: StudentMapper
) : StudentRepository {

    override fun save(domain: Student): Student {
        return mapper.domainToDocumentV1(domain)
            .let(studentMongoRepository::save)
            .let(mapper::documentToDomain)
    }

    override fun findById(id: String): Student {
        return studentMongoRepository
            .findById(id)
            .orElseThrow()
            .let(mapper::documentToDomain)
    }

    override fun findAllByName(name: String): List<Student> {
        return studentMongoRepository
            .findAllByName(name)
            .map(mapper::documentToDomain)
    }

    override fun findAll(): List<Student> {
        return studentMongoRepository
            .findAll()
            .map(mapper::documentToDomain)
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean {
        return studentMongoRepository.existsByNameAndPhoneAndBirth(name, phone, birthDate)
    }

}