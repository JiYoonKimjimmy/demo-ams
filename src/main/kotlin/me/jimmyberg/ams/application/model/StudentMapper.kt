package me.jimmyberg.ams.application.model

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.common.EMPTY
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.exception.InvalidRequestException
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate.SchoolPredicate
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.presentation.dto.StudentDTO
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun dtoToDomain(dto: StudentDTO): Student {
        requireNotNull(dto.name) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'name' is required") }
        requireNotNull(dto.phone) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'phone' is required") }
        requireNotNull(dto.birth) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'birth' is required") }
        requireNotNull(dto.gender) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'gender' is required") }

        return Student(
            id = dto.id,
            name = dto.name,
            phone = dto.phone,
            birth = dto.birth,
            gender = dto.gender,
            address = dto.address,
            school = dto.school,
            status = dto.status ?: ActivationStatus.REGISTER_WAITING
        )
    }

    fun domainToDTO(domain: Student): StudentDTO {
        return StudentDTO(
            id = domain.id,
            name = "${domain.name}${domain.nameLabel ?: EMPTY}",
            phone = domain.phone,
            birth = domain.birth,
            gender = domain.gender,
            zipCode = domain.address?.zipCode,
            baseAddress = domain.address?.baseAddress,
            detailAddress = domain.address?.detailAddress,
            schoolName = domain.school?.schoolName,
            schoolType = domain.school?.schoolType,
            grade = domain.school?.grade,
            status = domain.status
        )
    }

    fun dtoToPredicate(dto: StudentDTO, pageable: PageableRequest): StudentPredicate {
        return StudentPredicate(
            id = dto.id,
            name = dto.name,
            phone = dto.phone,
            birth = dto.birth,
            gender = dto.gender,
            school = SchoolPredicate(dto.schoolName, dto.schoolType, dto.grade),
            status = dto.status,
            pageable = pageable
        )
    }

}