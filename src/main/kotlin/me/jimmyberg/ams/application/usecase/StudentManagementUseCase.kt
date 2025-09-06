package me.jimmyberg.ams.application.usecase

import me.jimmyberg.ams.application.model.StudentMapper
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.port.inbound.StudentFindService
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.presentation.dto.StudentDTO
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Component
class StudentManagementUseCase(
    private val studentSaveService: StudentSaveService,
    private val studentFindService: StudentFindService,
    private val studentMapper: StudentMapper,
) {

    @Transactional
    fun createStudent(dto: StudentDTO): StudentDTO {
        return studentMapper.dtoToDomain(dto)
            .let { studentSaveService.save(student = it) }
            .let { studentMapper.domainToDTO(domain = it) }
    }

    fun findStudent(id: Long): StudentDTO {
        return studentFindService.findOne(predicate = StudentPredicate(id))
            .let { studentMapper.domainToDTO(domain = it) }
    }

    fun scrollStudents(dto: StudentDTO, pageable: PageableRequest): ScrollResult<StudentDTO> {
        return studentMapper.dtoToPredicate(dto, pageable)
            .let { studentFindService.scroll(predicate = it) }
            .let { ScrollResult.from(result = it, mapper = studentMapper::domainToDTO) }
    }

    @Transactional
    fun updateStudent(dto: StudentDTO): StudentDTO {
        return studentFindService.findOne(predicate = StudentPredicate(id = dto.id))
            .update(dto)
            .let { studentSaveService.save(student = it) }
            .let { studentMapper.domainToDTO(domain = it) }
    }

}