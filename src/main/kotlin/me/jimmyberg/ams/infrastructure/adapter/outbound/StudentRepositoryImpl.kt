package me.jimmyberg.ams.infrastructure.adapter.outbound

import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.StudentExposedRepository
import me.jimmyberg.ams.infrastructure.adapter.outbound.exposed.entity.StudentQuery
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val studentExposedRepository: StudentExposedRepository,
) : StudentRepository {

    override fun save(student: Student): Student {
        return studentExposedRepository.save(student)
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return studentExposedRepository.findBy(query = StudentQuery.of(predicate))
    }

    override fun findAllByPredicate(predicate: StudentPredicate): List<Student> {
        return studentExposedRepository.findAllBy(query = StudentQuery.of(predicate))
    }

    override fun scrollByPredicate(predicate: StudentPredicate): ScrollResult<Student> {
        return studentExposedRepository.scrollBy(query = StudentQuery.of(predicate))
            .let { ScrollResult.of(pair = it, mapper = { it }) }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return studentExposedRepository.findAllByNameAndPhoneAndBirth(name, phone, birth).isNotEmpty()
    }

}