package me.jimmyberg.ams.domain.service

import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.presentation.common.PageableRequest
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.FindStudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class FindStudentServiceImpl(
    private val studentRepository: StudentRepository
) : FindStudentService {

    override fun findOne(predicate: StudentPredicate): Student {
        return studentRepository.findByPredicate(predicate)
            ?: throw EntityNotFoundException("Student not found")
    }

    override fun scroll(predicate: StudentPredicate, pageable: PageableRequest): ScrollResult<Student> {
        return studentRepository.scrollByPredicate(predicate, pageable)
    }

}