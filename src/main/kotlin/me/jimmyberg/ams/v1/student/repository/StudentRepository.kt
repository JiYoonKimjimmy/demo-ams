package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.springframework.stereotype.Repository

@Repository
class StudentRepository(
    private val studentMongoRepository: StudentMongoRepository,
) : StudentRepositoryV1 {

    override fun save(document: StudentDocumentV1): StudentDocumentV1 {
        return studentMongoRepository.save(document)
    }

    override fun findById(id: String): StudentDocumentV1 {
        return studentMongoRepository
            .findById(id)
            .orElseThrow()
    }

    override fun findAllByName(name: String): List<StudentDocumentV1> {
        return studentMongoRepository
            .findAllByName(name)
    }

    override fun findAll(): List<StudentDocumentV1> {
        return studentMongoRepository
            .findAll()
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birthDate: String): Boolean {
        return studentMongoRepository.existsByNameAndPhoneAndBirth(name, phone, birthDate)
    }

    override fun findByPredicate(predicate: StudentPredicate): StudentDocumentV1 {
        TODO("Not yet implemented : https://velog.io/@ekxk1234/Spring-Custom-MongoDB-DSL%EC%9D%84-%EB%A7%8C%EB%93%A4%EB%A9%B0")
    }

}