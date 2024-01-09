package me.jimmyberg.ams.student.domain

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun documentToDomain(document: StudentDocumentV1): Student {
        return Student(
            id = document.id!!,
            name = document.name,
            phone = document.phone,
            birthday = document.birthday,
            gender = document.gender,
            address = document.address,
            schoolName = document.schoolName,
            schoolType = document.schoolType,
            grade = document.grade,
            status = document.status,
        )
    }

}