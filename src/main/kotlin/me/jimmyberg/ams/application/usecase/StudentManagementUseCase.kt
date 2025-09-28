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

    suspend fun createStudent(dto: StudentDTO): StudentDTO {
        val prepared = studentMapper.dtoToDomain(dto)
        val saved = tx { studentSaveService.save(student = prepared) }
        return studentMapper.domainToDTO(domain = saved)
    }

    suspend fun findStudent(id: Long): StudentDTO {
        val found = tx { studentFindService.findOne(predicate = StudentPredicate(id)) }
        return studentMapper.domainToDTO(domain = found)
    }

    suspend fun scrollStudents(dto: StudentDTO, pageable: PageableRequest): ScrollResult<StudentDTO> {
        val predicate = studentMapper.dtoToPredicate(dto, pageable)
        val result = tx { studentFindService.scroll(predicate = predicate) }
        return ScrollResult.from(result = result, mapper = studentMapper::domainToDTO)
    }

    suspend fun updateStudent(dto: StudentDTO): StudentDTO {
        val existing = tx { studentFindService.findOne(predicate = StudentPredicate(id = dto.id)) }
        val toSave = existing.update(dto)
        val saved = tx { studentSaveService.save(student = toSave) }
        return studentMapper.domainToDTO(domain = saved)
    }

}