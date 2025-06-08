package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.UpdateStudentService
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