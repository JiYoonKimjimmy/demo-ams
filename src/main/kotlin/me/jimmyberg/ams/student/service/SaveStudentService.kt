package me.jimmyberg.ams.student.service

import me.jimmyberg.ams.student.domain.Student
import me.jimmyberg.ams.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class SaveStudentService(
    private val studentRepository: StudentRepository
) {

    /**
     * 학생 정보 저장 처리
     */
    fun save(student: Student): Student {
        return studentRepository.save(student)
    }

}