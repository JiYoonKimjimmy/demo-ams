package me.jimmyberg.ams.application.model

import me.jimmyberg.ams.domain.model.Student
import me.jimmyberg.ams.common.EMPTY
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.exception.InvalidRequestException
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate.SchoolPredicate
import me.jimmyberg.ams.common.model.PageableRequest
import org.springframework.stereotype.Component

@Component
class StudentModelMapper {

    fun modelToDomain(model: StudentModel): Student {
        requireNotNull(model.name) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'name' is required") }
        requireNotNull(model.phone) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'phone' is required") }
        requireNotNull(model.birth) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'birth' is required") }
        requireNotNull(model.gender) { throw InvalidRequestException(ErrorCode.MISSING_REQUIRED_DATA, detailMessage = "Student 'gender' is required") }

        return Student(
            id = model.id,
            name = model.name,
            phone = model.phone,
            birth = model.birth,
            gender = model.gender,
            address = model.address,
            school = model.school,
            status = model.status ?: ActivationStatus.REGISTER_WAITING
        )
    }

    fun domainToModel(domain: Student): StudentModel {
        return StudentModel(
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

    fun modelToPredicate(model: StudentModel, pageable: PageableRequest): StudentPredicate {
        return StudentPredicate(
            id = model.id,
            name = model.name,
            phone = model.phone,
            birth = model.birth,
            gender = model.gender,
            school = SchoolPredicate(model.schoolName, model.schoolType, model.grade),
            status = model.status,
            pageable = pageable
        )
    }

}