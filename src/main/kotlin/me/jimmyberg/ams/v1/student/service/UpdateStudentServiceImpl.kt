package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.repository.StudentRepository
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UpdateStudentServiceImpl(
    private val studentRepository: StudentRepository
) : UpdateStudentService {

    override fun update(student: Student): Student {
        return studentRepository.save(student)
    }

}