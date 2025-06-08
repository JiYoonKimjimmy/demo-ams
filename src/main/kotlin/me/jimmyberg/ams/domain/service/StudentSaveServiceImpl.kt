package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import org.springframework.stereotype.Service

@Service
class StudentSaveServiceImpl(
    private val studentRepository: StudentRepository
) : StudentSaveService {

    override fun save(student: Student): Student {
        return student
            .checkDuplicateStudentByNameAndGetIndexOfName()
            .saveStudent()
    }

    private fun Student.checkDuplicateStudentByNameAndGetIndexOfName(): Student {
        // TODO : 코드 개선 필요
        val students = studentRepository.findAllByPredicate(StudentPredicate(name = name))
        val indexOfName = if (students.isNotEmpty()) {
            students.sortedBy { it.nameLabel }.last().nameLabel ?: 1
        } else {
            null
        }
        nameLabel = indexOfName?.inc()
        return this
    }

    private fun Student.saveStudent(): Student {
        return studentRepository.save(student = this)
    }

}