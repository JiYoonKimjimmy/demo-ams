package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class FindStudentService(
    private val studentRepository: StudentRepository
) {

    fun findAll(): List<Student> {
        return studentRepository.findAll()
    }

}