package me.jimmyberg.ams.v1.student.service

import me.jimmyberg.ams.v1.student.service.domain.Student
import me.jimmyberg.ams.v1.student.service.domain.StudentMapper
import me.jimmyberg.ams.v1.student.repository.StudentRepositoryV1
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UpdateStudentServiceImpl(
    private val studentRepository: StudentRepositoryV1,
    private val mapper: StudentMapper
) : UpdateStudentService {

    override fun update(student: Student): Student {
        return mapper.domainToDocumentV1(student)
            .let(studentRepository::save)
            .let(mapper::documentToDomain)
    }

}