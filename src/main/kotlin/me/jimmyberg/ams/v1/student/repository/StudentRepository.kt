package me.jimmyberg.ams.v1.student.repository

import com.example.kotlinmongo.extension.document
import com.example.kotlinmongo.extension.field
import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.v1.student.repository.document.StudentDocumentV1
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class StudentRepository(
    private val studentMongoRepository: StudentMongoRepository,
    private val mongoTemplate: MongoTemplate,
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
        val query = document {
            and(
                { field(StudentDocumentV1::name) eq predicate.name!! }
            )
        }
        return mongoTemplate.findOne(query, StudentDocumentV1::class.java) ?: throw EntityNotFoundException()
    }

}