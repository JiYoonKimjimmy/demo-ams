package me.jimmyberg.ams.application.usecase

import me.jimmyberg.ams.application.model.StudentMapper
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.common.model.ScrollResult
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import me.jimmyberg.ams.domain.port.inbound.StudentFindService
import me.jimmyberg.ams.domain.port.inbound.StudentSaveService
import me.jimmyberg.ams.presentation.dto.StudentDTO
import org.springframework.stereotype.Component
import me.jimmyberg.ams.infrastructure.config.tx

@Component
class StudentManagementUseCase(
    private val studentSaveService: StudentSaveService,
    private val studentFindService: StudentFindService,
    private val studentMapper: StudentMapper,
) {

    suspend fun createStudent(dto: StudentDTO): StudentDTO = tx {
        studentMapper.dtoToDomain(dto)
            .let { studentSaveService.save(student = it) }
            .let { studentMapper.domainToDTO(domain = it) }
    }

    suspend fun findStudent(id: Long): StudentDTO = tx {
        studentFindService.findOne(predicate = StudentPredicate(id))
            .let { studentMapper.domainToDTO(domain = it) }
    }

    suspend fun scrollStudents(dto: StudentDTO, pageable: PageableRequest): ScrollResult<StudentDTO> = tx {
        studentMapper.dtoToPredicate(dto, pageable)
            .let { studentFindService.scroll(predicate = it) }
            .let { ScrollResult.from(result = it, mapper = studentMapper::domainToDTO) }
    }

    suspend fun updateStudent(dto: StudentDTO): StudentDTO = tx {
        studentFindService.findOne(predicate = StudentPredicate(id = dto.id))
            .update(dto)
            .let { studentSaveService.save(student = it) }
            .let { studentMapper.domainToDTO(domain = it) }
    }

}