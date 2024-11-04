package me.jimmyberg.ams.v1.student.service

import jakarta.persistence.EntityNotFoundException
import me.jimmyberg.ams.v1.student.repository.StudentRepository
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student
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

    override fun findAll(): List<Student> {
        return studentRepository.findAll()
    }

}