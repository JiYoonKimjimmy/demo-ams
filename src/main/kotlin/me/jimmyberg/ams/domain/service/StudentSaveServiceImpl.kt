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
            .assignNextNameLabel()
            .saveStudent()
    }

    private fun Student.assignNextNameLabel(): Student {
        val lastNameLabel = studentRepository
            .findAllByPredicate(StudentPredicate(name = name))
            .maxOfOrNull { it.nameLabel ?: 1 }

        return copy(nameLabel = lastNameLabel?.inc())
    }

    private fun Student.saveStudent(): Student {
        return studentRepository.save(student = this)
    }

}