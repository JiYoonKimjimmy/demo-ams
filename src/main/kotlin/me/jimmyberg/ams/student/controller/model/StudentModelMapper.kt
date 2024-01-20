package me.jimmyberg.ams.student.controller.model

import me.jimmyberg.ams.student.domain.Student
import org.springframework.stereotype.Component

@Component
class StudentModelMapper {

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