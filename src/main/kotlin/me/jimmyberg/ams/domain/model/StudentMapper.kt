package me.jimmyberg.ams.domain.model

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.infrastructure.common.EMPTY
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate
import me.jimmyberg.ams.infrastructure.repository.exposed.StudentPredicate.SchoolPredicate
import org.springframework.stereotype.Component

@Component
class StudentMapper {

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

}