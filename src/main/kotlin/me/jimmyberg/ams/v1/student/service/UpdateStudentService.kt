package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UpdateStudentService(
    private val studentRepository: StudentRepository,
    private val mapper: StudentMapper
) : UpdateStudentServiceV1 {

    override fun update(student: Student): StudentModel {
        return studentRepository.save(student).let(mapper::domainToModel)
    }

}