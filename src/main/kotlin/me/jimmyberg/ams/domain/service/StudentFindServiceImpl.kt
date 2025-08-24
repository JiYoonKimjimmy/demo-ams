package me.jimmyberg.ams.domain.service

import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.StudentFindService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import org.springframework.stereotype.Service

@Service
class StudentFindServiceImpl(
    private val studentRepository: StudentRepository
) : StudentFindService {

    override fun findOne(predicate: StudentPredicate): Student {
        return studentRepository.findByPredicate(predicate)
            ?: throw EntityNotFoundException("Student not found")
    }

    override fun scroll(predicate: StudentPredicate): ScrollResult<Student> {
        return studentRepository.scrollByPredicate(predicate)
    }

}