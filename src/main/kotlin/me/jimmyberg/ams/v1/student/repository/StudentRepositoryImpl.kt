package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.mongodsl.extension.findAll
import me.jimmyberg.ams.mongodsl.extension.findOne
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val studentMapper: StudentMapper,
    private val studentMongoRepository: StudentMongoRepository,
    private val mongoTemplate: MongoTemplate,
) : StudentRepository {

    override fun save(domain: Student): Student {
        return studentMapper.domainToDocumentV1(domain)
            .let { studentMongoRepository.save(it) }
            .let { studentMapper.documentToDomain(it) }
    }

    override fun findAll(): List<Student> {
        return studentMongoRepository.findAll()
            .map { studentMapper.documentToDomain(it) }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return studentMongoRepository.existsByNameAndPhoneAndBirth(name, phone, birth)
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return mongoTemplate.findOne(predicate.query, Student::class)
    }

    override fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest): List<Student> {
        return mongoTemplate.findAll(predicate.query, pageable.toPageRequest(), Student::class)
    }

}