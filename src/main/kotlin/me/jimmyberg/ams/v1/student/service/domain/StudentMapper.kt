package me.jimmyberg.ams.v1.student.service.domain

import me.jimmyberg.ams.v1.student.controller.model.ScrollStudentsRequest
import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import me.jimmyberg.ams.v1.student.repository.document.StudentDocument
import me.jimmyberg.ams.v1.student.repository.entity.StudentEntity
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate.SchoolPredicate
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun requestToPredicate(request: ScrollStudentsRequest): StudentPredicate {
        return StudentPredicate(
            id = request.id,
            name = request.name,
            phone = request.phone,
            birth = request.birth,
            gender = request.gender,
            school = SchoolPredicate(request.schoolName, request.schoolType, request.grade),
            status = request.status
        )
    }

    fun modelToDomain(model: StudentModel): Student {
        return Student(
            id = model.id?.toLong(),
            name = model.name,
            phone = model.phone,
            birth = model.birth,
            gender = model.gender,
            address = model.address,
            school = model.school,
            status = model.status
        )
    }

    fun domainToModel(domain: Student): StudentModel {
        return StudentModel(
            id = domain.id?.toString(),
            name = "${domain.name}${domain.nameLabel ?: ""}",
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

    fun domainToDocument(domain: Student): StudentDocument {
        return StudentDocument(
            id = domain.id?.toString(),
            name = domain.name,
            indexOfName = domain.nameLabel,
            phone = domain.phone,
            birth = domain.birth,
            gender = domain.gender,
            address = domain.address,
            school = domain.school,
            status = domain.status,
        )
    }

    fun documentToDomain(document: StudentDocument): Student {
        return Student(
            id = document.id!!.toLong(),
            name = document.name,
            nameLabel = document.indexOfName,
            phone = document.phone,
            birth = document.birth,
            gender = document.gender,
            address = document.address,
            school = document.school,
            status = document.status
        )
    }

    fun entityToDomain(entity: StudentEntity): Student {
        return Student(
            id = entity.id.value,
            name = entity.name,
            nameLabel = entity.nameLabel?.toInt(),
            phone = entity.phone,
            birth = entity.birth,
            gender = entity.gender,
            address = entity.address,
            school = entity.school,
            status = entity.status
        )
    }

}