package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.controller.model.StudentModel
import me.jimmyberg.ams.student.controller.model.StudentModelMapper
import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class FindStudentService(
    private val studentRepository: StudentRepository,
    private val mapper: StudentModelMapper
) {

    fun findOne(id: String): StudentModel {
        return studentRepository.findById(id).let(mapper::domainToModel)
    }

    fun findAll(): List<StudentModel> {
        return studentRepository.findAll().map(mapper::domainToModel)
    }

}