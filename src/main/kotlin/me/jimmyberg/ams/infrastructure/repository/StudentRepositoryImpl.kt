package me.jimmyberg.ams.infrastructure.repository

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.presentation.common.PageableRequest
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentExposedRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentEntity
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val studentExposedRepository: StudentExposedRepository,
) : StudentRepository {

    override fun save(student: Student): Student {
        return studentExposedRepository.save(student).toDomain()
    }

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return studentExposedRepository.findByPredicate(predicate)?.toDomain()
    }

    override fun findAllByPredicate(predicate: StudentPredicate): List<Student> {
        return studentExposedRepository.findAllByPredicate(predicate).map { it.toDomain() }
    }

    override fun scrollByPredicate(predicate: StudentPredicate): ScrollResult<Student> {
        return studentExposedRepository.scrollByPredicate(predicate)
            .let {
                ScrollResult(
                    content = it.first.map(transform = StudentEntity::toDomain),
                    size = it.first.size,
                    isEmpty = it.first.isEmpty(),
                    hasNext = it.second,
                )
            }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return studentExposedRepository.findAllByNameAndPhoneAndBirth(name, phone, birth)
            .isNotEmpty()
    }

}