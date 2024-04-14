package me.jimmyberg.ams.v1.student.controller.model

data class FindStudentResponse(
    val student: StudentModel
)

data class FindAllStudentResponse(
    val students: List<StudentModel>
)