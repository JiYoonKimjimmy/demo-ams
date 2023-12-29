package me.jimmyberg.ams.student.domain

import me.jimmyberg.ams.student.document.mongo.StudentDocumentV1
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun documentToDomain(document: StudentDocumentV1): Student {
        return Student(name = document.name)
    }

}