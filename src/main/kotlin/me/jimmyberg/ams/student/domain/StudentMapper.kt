package me.jimmyberg.ams.student.domain

import me.jimmyberg.ams.student.document.mongo.StudentDocument
import org.springframework.stereotype.Component

@Component
class StudentMapper {

    fun documentToDomain(document: StudentDocument): Student {
        return Student(name = document.getName())
    }

}