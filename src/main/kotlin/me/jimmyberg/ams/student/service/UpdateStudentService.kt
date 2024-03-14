package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.domain.StudentMapper
import me.jimmyberg.ams.student.repository.StudentRepository
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