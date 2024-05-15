package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.domain.Student
import me.jimmyberg.ams.v1.student.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class SaveStudentService(
    private val studentRepository: StudentRepository,
    private val mapper: StudentMapper
) : SaveStudentServiceV1 {

    override fun save(student: Student): StudentModel {
        return with(student) {
            // 1. 동일한 이름 & 생년월일 & 휴대폰번호 학생 정보 등록 여부 확인
            checkDuplicateStudentByNameAndPhoneAndBirth(this)
            // 2. 동일한 이름 학생 정보 등록 여부 확인 & indexOfName 생성
            checkDuplicateStudentByNameAndGetIndexOfName(this)
            // 3. 학생 정보 저장 & 반환 처리
            studentRepository.save(this).let(mapper::domainToModel)
        }
    }

    private fun checkDuplicateStudentByNameAndPhoneAndBirth(student: Student) {
        if (studentRepository.isExistByNameAndPhoneAndBirth(student.name, student.phone, student.birth)) {
            throw IllegalArgumentException()
        }
    }

    private fun checkDuplicateStudentByNameAndGetIndexOfName(student: Student): Student {
        val students = studentRepository.findAllByName(student.name)
        val indexOfName = if (students.isNotEmpty()) {
            students.sortedBy { it.indexOfName }.last().indexOfName ?: 1
        } else {
            null
        }
        return student.apply { this.indexOfName = indexOfName?.inc() }
    }

}