package me.jimmyberg.ams.v1.student.controller.model

data class SaveStudentRequest(
    val student: StudentModel
)

data class SaveStudentResponse(
    val student: StudentModel
)