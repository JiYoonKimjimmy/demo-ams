package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class FindStudentService(
    private val studentRepository: StudentRepository,
    private val mapper: StudentMapper
) : FindStudentServiceV1 {

    override fun findOne(id: String): StudentModel {
        return studentRepository.findById(id).let(mapper::domainToModel)
    }

    override fun findAll(): List<StudentModel> {
        return studentRepository.findAll().map(mapper::domainToModel)
    }

}