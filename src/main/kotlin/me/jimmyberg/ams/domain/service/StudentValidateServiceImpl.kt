package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.StudentValidateService
import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentValidateServiceImpl(
    private val studentRepository: StudentRepository,
) : StudentValidateService {

    override fun validate(student: Student) {
        if (student.isExistByNameAndPhoneAndBirth()) {
            throw IllegalArgumentException("Student with ${student.name}, ${student.phone}, ${student.birth} already exists.")
        }
    }

    private fun Student.isExistByNameAndPhoneAndBirth(): Boolean {
        // 동일한 학생 `name`, `phone`, `birth` 이미 등록 여부 확인
        return studentRepository.isExistByNameAndPhoneAndBirth(this.name, this.phone, this.birth)
    }

}