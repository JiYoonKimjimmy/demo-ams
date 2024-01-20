package me.jimmyberg.ams.student.controller.model

import me.jimmyberg.ams.student.domain.Student

data class SaveStudentRequest(
    val student: StudentModel
)

data class SaveStudentResponse(
    val student: StudentModel
)