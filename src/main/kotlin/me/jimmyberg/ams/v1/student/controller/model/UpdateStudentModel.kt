package me.jimmyberg.ams.v1.student.controller.model

data class UpdateStudentRequest(
    val student: StudentModel
)

data class UpdateStudentResponse(
    val student: StudentModel
)