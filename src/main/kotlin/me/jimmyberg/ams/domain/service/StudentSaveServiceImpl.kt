package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.InvalidRequestException
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentSaveServiceImpl(
    private val studentRepository: StudentRepository
) : StudentSaveService {

    override fun save(student: Student): Student {
        return student
            .validateStudent()
            .assignNextNameLabel()
            .saveStudent()
    }

    private fun Student.validateStudent(): Student {
        if (isExistSameStudentInfo()) {
            throw InvalidRequestException(ErrorCode.STUDENT_INFO_DUPLICATED)
        }
        return this
    }

    private fun Student.isExistSameStudentInfo(): Boolean {
        // 동일한 학생 `name`, `phone`, `birth` 이미 등록 여부 확인
        return studentRepository.isExistByNameAndPhoneAndBirth(this.name, this.phone, this.birth)
    }

    private fun Student.assignNextNameLabel(): Student {
        val lastNameLabel = studentRepository
            .findAllByPredicate(predicate = StudentPredicate(name = name))
            .maxOfOrNull { it.nameLabel ?: 1 }

        return copy(nameLabel = lastNameLabel?.inc())
    }

    private fun Student.saveStudent(): Student {
        return studentRepository.save(student = this)
    }

}