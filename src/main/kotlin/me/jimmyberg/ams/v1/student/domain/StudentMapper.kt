package me.jimmyberg.ams.v1.student.domain

import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.document.mongo.StudentDocumentV1
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun modelToDomain(model: StudentModel): Student {
        return Student(
            id = model.id,
            name = model.name,
            phone = model.phone,
            birthday = model.birthday,
            gender = model.gender,
            address = model.address,
            school = model.school,
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
            zipCode = domain.address?.zipCode,
            baseAddress = domain.address?.baseAddress,
            detailAddress = domain.address?.detailAddress,
            schoolName = domain.school.schoolName,
            schoolType = domain.school.schoolType,
            grade = domain.school.grade,
            status = domain.status
        )
    }

    fun domainToDocumentV1(domain: Student): StudentDocumentV1 {
        return StudentDocumentV1(
            id = domain.id,
            name = domain.name,
            phone = domain.phone,
            birthday = domain.birthday,
            gender = domain.gender,
            address = domain.address,
            school = domain.school,
            status = domain.status,
        )
    }

    fun documentToDomain(document: StudentDocumentV1): Student {
        return Student(
            id = document.id!!,
            name = document.name,
            phone = document.phone,
            birthday = document.birthday,
            gender = document.gender,
            address = document.address,
            school = document.school,
            status = document.status
        )
    }

}