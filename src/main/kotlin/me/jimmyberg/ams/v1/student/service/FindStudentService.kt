package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepositoryV1
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class FindStudentService(
    private val studentRepository: StudentRepositoryV1,
    private val mapper: StudentMapper
) : FindStudentServiceV1 {

    override fun findOne(id: String): Student {
        return studentRepository.findById(id).let(mapper::documentToDomain)
    }

    override fun findAll(): List<Student> {
        return studentRepository.findAll().map(mapper::documentToDomain)
    }

}