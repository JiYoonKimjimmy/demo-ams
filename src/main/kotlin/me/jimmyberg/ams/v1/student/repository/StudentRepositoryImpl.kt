package me.jimmyberg.ams.v1.student.repository

import me.jimmyberg.ams.common.domain.ScrollContent
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

    override fun findByPredicate(predicate: StudentPredicate): Student? {
        return studentExposedRepository.findByPredicate(predicate)
            ?.let { studentMapper.entityToDomain(it) }
    }

    override fun findAllByPredicate(predicate: StudentPredicate, pageable: PageableRequest): List<Student> {
        return studentExposedRepository.findAllByPredicate(predicate)
            .map { studentMapper.entityToDomain(it) }
    }

    override fun scrollByPredicate(predicate: StudentPredicate, pageable: PageableRequest): ScrollContent<Student> {
        return studentExposedRepository.scrollByPredicate(predicate, pageable)
            .let { ScrollContent.from(it.first, it.second, studentMapper::entityToDomain) }
    }

    override fun isExistByNameAndPhoneAndBirth(name: String, phone: String, birth: String): Boolean {
        return studentExposedRepository.findAllByNameAndPhoneAndBirth(name, phone, birth)
            .isNotEmpty()
    }

}