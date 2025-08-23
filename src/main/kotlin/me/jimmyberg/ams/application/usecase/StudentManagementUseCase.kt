package me.jimmyberg.ams.application.usecase

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.application.usecase.model.StudentModelMapper
import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.domain.port.inbound.StudentFindService
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.presentation.common.PageableRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Component
class StudentManagementUseCase(
    private val studentSaveService: StudentSaveService,
    private val studentFindService: StudentFindService,
    private val studentModelMapper: StudentModelMapper,
) {

    @Transactional
    fun createStudent(model: StudentModel): StudentModel {
        return studentModelMapper.modelToDomain(model)
            .let { studentSaveService.save(student = it) }
            .let { studentModelMapper.domainToModel(domain = it) }
    }

    fun findStudent(id: Long): StudentModel {
        return StudentPredicate(id)
            .let { studentFindService.findOne(predicate = it) }
            .let { studentModelMapper.domainToModel(domain = it) }
    }

    fun scrollStudents(model: StudentModel, pageable: PageableRequest): ScrollResult<StudentModel> {
        val predicate = studentModelMapper.modelToPredicate(model)
        return ScrollResult.from(
            result = studentFindService.scroll(predicate, pageable),
            mapper = studentModelMapper::domainToModel
        )
    }

    @Transactional
    fun updateStudent(model: StudentModel): StudentModel {
        return StudentPredicate(id = model.id)
            .let { studentFindService.findOne(predicate = it) }
            .update(model = model)
            .let { studentSaveService.save(student = it) }
            .let { studentModelMapper.domainToModel(domain = it) }
    }

}