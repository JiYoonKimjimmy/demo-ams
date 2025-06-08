package me.jimmyberg.ams.domain.service

import me.jimmyberg.ams.domain.port.outbound.StudentRepository
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.domain.port.inbound.SaveStudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class SaveStudentServiceImpl(
    private val studentRepository: StudentRepository
) : SaveStudentService {

    override fun save(student: Student): Student {
        return with(student) {
            // 1. 동일한 이름 & 생년월일 & 휴대폰번호 학생 정보 등록 여부 확인
            checkDuplicateStudentByNameAndPhoneAndBirth(this)
            // 2. 동일한 이름 학생 정보 등록 여부 확인 & indexOfName 생성
            checkDuplicateStudentByNameAndGetIndexOfName(this)
            // 3. 학생 document 저장
            saveStudentDocument(this)
        }
    }

    private fun checkDuplicateStudentByNameAndPhoneAndBirth(student: Student) {
        if (studentRepository.isExistByNameAndPhoneAndBirth(student.name, student.phone, student.birth)) {
            throw IllegalArgumentException("Student with ${student.name}, ${student.phone}, ${student.birth} already exists.")
        }
    }

    private fun checkDuplicateStudentByNameAndGetIndexOfName(student: Student): Student {
        val students = studentRepository.findAllByPredicate(StudentPredicate(name = student.name))
        val indexOfName = if (students.isNotEmpty()) {
            students.sortedBy { it.nameLabel }.last().nameLabel ?: 1
        } else {
            null
        }
        return student.apply { this.nameLabel = indexOfName?.inc() }
    }

    private fun saveStudentDocument(student: Student): Student {
        return studentRepository.save(student)
    }

}