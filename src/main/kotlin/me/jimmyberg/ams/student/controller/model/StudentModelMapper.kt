package me.jimmyberg.ams.student.controller.model

import me.jimmyberg.ams.student.domain.Student
import org.springframework.stereotype.Component

@Component
class StudentModelMapper {

    fun modelToDomain(model: StudentModel): Student {
        return Student(
            id = model.id,
            name = model.name,
            phone = model.phone,
            birthday = model.birthday,
            gender = model.gender,
            address = model.address,
            schoolName = model.schoolName,
            schoolType = model.schoolType,
            grade = model.grade,
            status = model.status
        )
    }

    fun domainToModel(domain: Student): StudentModel {
        return StudentModel(
            id = domain.id,
            name = domain.name,
            phone = domain.phone,
            birthday = domain.birthday,
            gender = domain.gender,
            address = domain.address,
            schoolName = domain.schoolName,
            schoolType = domain.schoolType,
            grade = domain.grade,
            status = domain.status
        )
    }
}