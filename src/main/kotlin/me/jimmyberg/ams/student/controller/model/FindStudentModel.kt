package me.jimmyberg.ams.student.controller.model

data class FindStudentResponse(
    val student: StudentModel
)

data class FindAllStudentResponse(
    val students: List<StudentModel>
)