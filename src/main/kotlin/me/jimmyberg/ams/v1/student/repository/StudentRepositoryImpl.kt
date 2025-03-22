package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.domain.PageableContent
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val studentMapper: StudentMapper,
    private val studentExposedRepository: StudentExposedRepository,
) : StudentRepository {

    override fun save(student: Student): Student {
        return studentExposedRepository.save(student)
            .let { studentMapper.entityToDomain(it) }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return studentExposedRepository.findAllByNameAndPhoneAndBirth(name, phone, birth)
            .isNotEmpty()
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return studentExposedRepository.findByPredicate(predicate)
            ?.let { studentMapper.entityToDomain(it) }
    }

    override fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest): List<Student> {
        TODO("Not yet implemented")
    }

    override fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest): PageableContent<Student> {
        TODO("Not yet implemented")
    }

//    override fun findByPredicate(predicate: StudentPredicate): Student? {
//        return mongoTemplate.findOne(predicate.query, StudentDocument::class)
//            ?.let { studentMapper.documentToDomain(it) }
//    }
//
//    override fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest): List<Student> {
//        return mongoTemplate.findAll(predicate.query, pageable.toPageRequest(), StudentDocument::class)
//            .map { studentMapper.documentToDomain(it) }
//    }
//
//    override fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest): PageableContent<Student> {
//        return mongoTemplate.scroll(predicate.query, pageable.toPageRequest(), StudentDocument::class)
//            .let { PageableContent.from(it, studentMapper::documentToDomain) }
//    }

}