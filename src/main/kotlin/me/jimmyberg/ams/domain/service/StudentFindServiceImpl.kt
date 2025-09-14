package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import me.jimmyberg.ams.domain.port.inbound.StudentFindService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository

@Service
class StudentFindServiceImpl(
    private val studentRepository: StudentRepository
) : StudentFindService {

    override fun findOne(predicate: StudentPredicate): Student {
        return studentRepository.findByPredicate(predicate)
            ?: throw ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
    }

    override fun scroll(predicate: StudentPredicate): ScrollResult<Student> {
        return studentRepository.scrollByPredicate(predicate)
    }

}