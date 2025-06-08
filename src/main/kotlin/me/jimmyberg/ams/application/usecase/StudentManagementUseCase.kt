package me.jimmyberg.ams.application.usecase

import me.jimmyberg.ams.infrastructure.common.domain.ScrollResult
import me.jimmyberg.ams.infrastructure.common.model.PageableRequest
import me.jimmyberg.ams.domain.model.StudentMapper
import me.jimmyberg.ams.domain.port.inbound.FindStudentService
import me.jimmyberg.ams.domain.port.inbound.SaveStudentService
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.application.usecase.model.StudentModel
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StudentManagementUseCase(
    private val studentMapper: StudentMapper,
    private val saveStudentService: SaveStudentService,
    private val findStudentService: FindStudentService
) {

    @Transactional
    fun saveStudent(model: StudentModel): StudentModel {
        return studentMapper.modelToDomain(model)
            .let { saveStudentService.save(student = it) }
            .let { studentMapper.domainToModel(domain = it) }
    }

    fun findStudent(id: String): StudentModel {
        return StudentPredicate(id = id)
            .let { findStudentService.findOne(it) }
            .let { studentMapper.domainToModel(it) }
    }

    fun scrollStudents(model: StudentModel, pageable: PageableRequest): ScrollResult<StudentModel> {
        val predicate = studentMapper.modelToPredicate(model)
        return ScrollResult.from(
            result = findStudentService.scroll(predicate, pageable),
            mapper = studentMapper::domainToModel
        )
    }

    @Transactional
    fun updateStudent(model: StudentModel): StudentModel {
        return StudentPredicate(id = model.id)
            .let { findStudentService.findOne(predicate = it) }
            .update(model = model)
            .let { saveStudentService.save(student = it) }
            .let { studentMapper.domainToModel(domain = it) }
    }

}