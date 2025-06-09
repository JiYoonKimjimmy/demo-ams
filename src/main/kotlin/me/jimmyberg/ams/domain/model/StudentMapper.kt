package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.infrastructure.common.EMPTY
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.exception.InvalidRequestException
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate.SchoolPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.entity.StudentEntity
import me.jimmyberg.ams.presentation.dto.ScrollStudentsRequest
import me.jimmyberg.ams.application.usecase.model.StudentModel
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun requestToModel(request: ScrollStudentsRequest): StudentModel {
        return StudentModel(
            id = request.id,
            name = request.name,
            phone = request.phone,
            birth = request.birth,
            gender = request.gender,
            schoolName = request.schoolName,
            schoolType = request.schoolType,
            grade = request.grade,
            status = request.status
        )
    }

    fun modelToPredicate(model: StudentModel): StudentPredicate {
        return StudentPredicate(
            id = model.id,
            name = model.name,
            phone = model.phone,
            birth = model.birth,
            gender = model.gender,
            school = SchoolPredicate(model.schoolName, model.schoolType, model.grade),
            status = model.status
        )
    }

    fun domainToModel(domain: Student): StudentModel {
        return StudentModel(
            id = domain.id?.toString(),
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

}