package me.jimmyberg.ams.student.controller.model

data class SaveStudentRequest(
    val student: StudentModel
)

data class SaveStudentResponse(
    val student: StudentModel
)