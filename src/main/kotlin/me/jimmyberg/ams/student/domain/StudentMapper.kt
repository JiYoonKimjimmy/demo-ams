package me.jimmyberg.ams.student.domain

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import org.springframework.stereotype.Component

@Component
class StudentMapper {

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