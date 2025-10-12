package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.common.error.ErrorCode
import me.jimmyberg.ams.common.error.exception.InvalidRequestException
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentSaveServiceImpl(
    private val studentRepository: StudentRepository
) : StudentSaveService {

    override fun save(student: Student): Student {
        val save = if (student.isNew()) {
            student.validateOnCreate()
        } else {
            student.validateOnUpdate()
        }

        return save
            .assignNextNameLabel()
            .saveStudent()
    }

    private fun Student.validateOnCreate(): Student {
        if (studentRepository.isExistByNameAndPhoneAndBirth(name, phone, birth)) {
            // 동일한 학생 `name`, `phone`, `birth` 이미 등록 여부 확인
            throw InvalidRequestException(ErrorCode.STUDENT_INFO_DUPLICATED)
        }
        return this
    }

    private fun Student.validateOnUpdate(): Student {
        val currentId = id ?: return this
        if (studentRepository.isExistByNameAndPhoneAndBirthExceptId(name, phone, birth, currentId)) {
            // 동일한 학생 `name`, `phone`, `birth` 이미 등록 여부 확인
            throw InvalidRequestException(ErrorCode.STUDENT_INFO_DUPLICATED)
        }
        return this
    }

    private fun Student.assignNextNameLabel(): Student {
        val lastNameLabel = studentRepository.findMaxNameLabelByName(name)
        return copy(nameLabel = lastNameLabel?.inc())
    }

    private fun Student.saveStudent(): Student {
        return studentRepository.save(student = this)
    }

}